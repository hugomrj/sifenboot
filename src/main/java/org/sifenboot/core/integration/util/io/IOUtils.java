package org.sifenboot.core.integration.util.io;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class IOUtils {

    public byte[] getByteArrayFromInputStream(InputStream inputStream)
            throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }

        return result.toByteArray();
    }

    public static String buildUrlParams(Map<String, String> params) {

        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            sb.append(e.getKey())
                    .append("=")
                    .append(e.getValue())
                    .append("&");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public static byte[] compressXmlToZip(String xml) {

        String fileName = "DE_" +
                new SimpleDateFormat("ddMMyyyy").format(new Date());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            zipOut.putNextEntry(new ZipEntry(fileName + ".xml"));
            zipOut.write(xml.getBytes(StandardCharsets.UTF_8));
            zipOut.closeEntry();

            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al comprimir XML en ZIP", e
            );
        }
    }
}
