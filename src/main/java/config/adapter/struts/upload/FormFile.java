package config.adapter.struts.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

// TODO: STV not yet implement
public class FormFile {

    private final MultipartFile file;

    public FormFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileName() {
        return file.getOriginalFilename();
    }

    public byte[] getFileData() throws Exception {
        return file.getBytes();
    }

    public InputStream getInputStream() throws Exception {
        return file.getInputStream();
    }

    public String getContentType() {
        return file.getContentType();
    }

    public int getFileSize() {
        return (int) file.getSize();
    }

    public void destroy() {
        // Struts requires destroy() but no-op here
    }
}
