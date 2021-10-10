package com.home.cameratomjpeg.controller;

import com.home.cameratomjpeg.service.CameraStreamService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

@Controller
@AllArgsConstructor
public class MjpegController {

    private CameraStreamService cameraStreamService;

    @GetMapping(value = "/camera/{cameraId}")
    @ResponseBody
    public void getCameraStream(@PathVariable String cameraId, HttpServletResponse response) throws IOException {
        response.setContentType("multipart/x-mixed-replace; boundary=--BoundaryString");

        ServletOutputStream outputStream = response.getOutputStream();

        BiConsumer<InputStream, Long> partsConsumer =
                (inputStream, length) -> writeBoundary(outputStream, inputStream, length);

        cameraStreamService.writeCameraSnapshot(cameraId, partsConsumer);
    }

    private void writeBoundary(ServletOutputStream outputStream, InputStream inputStream, long length) {
        try {
            outputStream.println("--BoundaryString");
            outputStream.println("Content-type: image/jpeg");
            outputStream.println("Content-Length: "+length);
            outputStream.println();

            IOUtils.copy(inputStream, outputStream);
            outputStream.println();
            outputStream.println();

            outputStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Error while write parts.", e);
        }
    }
}
