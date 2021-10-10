package com.home.cameratomjpeg.controller;

import com.home.cameratomjpeg.service.CameraStreamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Slf4j
@Controller
@AllArgsConstructor
public class MjpegController {

    private CameraStreamService cameraStreamService;
    private AtomicInteger atomicInteger;

    @GetMapping(value = "/camera/{cameraId}")
    @ResponseBody
    public void getCameraStream(@PathVariable String cameraId, HttpServletResponse response) {
        response.setContentType("multipart/x-mixed-replace; boundary=--BoundaryString");

        log.info("User connected. User count: {}", atomicInteger.incrementAndGet());

        try {
            ServletOutputStream outputStream = response.getOutputStream();

            BiConsumer<InputStream, Long> partsConsumer =
                    (inputStream, length) -> writeBoundary(outputStream, inputStream, length);

            cameraStreamService.writeCameraSnapshot(cameraId, partsConsumer);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            log.info("User disconnected. User count: {}", atomicInteger.decrementAndGet());
        }
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
