package com.list_project.lsit.Services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class StorageServiceTest {

    @Mock
    private Storage mockStorage;

    @InjectMocks
    private StorageService storageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storageService = new StorageService(mockStorage, bucketName);
    }

    @Test
    void testUploadFile() {
        String key = "test-key";
        byte[] content = "Test content".getBytes();

        BlobId blobId = BlobId.of(bucketName, key);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        when(mockStorage.create(blobInfo, content)).thenReturn(mock(Blob.class));

        assertDoesNotThrow(() -> storageService.uploadFile(key, content));
        verify(mockStorage, times(1)).create(blobInfo, content);
    }

    @Test
    void testDownloadFile() {
        String key = "test-key";
        BlobId blobId = BlobId.of(bucketName, key);
        Blob mockBlob = mock(Blob.class);

        when(mockStorage.get(blobId)).thenReturn(mockBlob);
        when(mockBlob.getContent()).thenReturn("Test content".getBytes());

        byte[] result = storageService.downloadFile(key);
        assertArrayEquals("Test content".getBytes(), result);
        verify(mockStorage, times(1)).get(blobId);
    }

    @Test
    void testDeleteFile() {
        String key = "test-key";
        BlobId blobId = BlobId.of(bucketName, key);

        when(mockStorage.delete(blobId)).thenReturn(true);

        assertDoesNotThrow(() -> storageService.deleteFile(key));
        verify(mockStorage, times(1)).delete(blobId);
    }

    @Test
    void testListFiles() {
        String prefix = "test-prefix/";
        Blob blob1 = mock(Blob.class);
        Blob blob2 = mock(Blob.class);

        when(blob1.getName()).thenReturn("test-prefix/file1.txt");
        when(blob2.getName()).thenReturn("test-prefix/file2.txt");

        Page<Blob> mockPage = mock(Page.class);
        when(mockPage.iterateAll()).thenReturn(List.of(blob1, blob2));
        when(mockStorage.list(eq(bucketName), any(Storage.BlobListOption.class))).thenReturn(mockPage);

        List<String> files = storageService.listFiles(prefix);
        assertEquals(2, files.size());
        assertTrue(files.contains("test-prefix/file1.txt"));
        assertTrue(files.contains("test-prefix/file2.txt"));

        verify(mockStorage, times(1)).list(eq(bucketName), any(Storage.BlobListOption.class));
    }
}
