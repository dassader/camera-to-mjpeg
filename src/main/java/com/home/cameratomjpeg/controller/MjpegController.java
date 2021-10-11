package com.home.cameratomjpeg.controller;

import com.home.cameratomjpeg.exception.JustSkipMeException;
import com.home.cameratomjpeg.service.CameraStreamService;
import com.home.cameratomjpeg.service.UserCountService;
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
import java.util.function.BiConsumer;

@Slf4j
@Controller
@AllArgsConstructor
public class MjpegController {

    private CameraStreamService cameraStreamService;
    private UserCountService userCountService;

    @GetMapping(value = "/camera/{cameraId}")
    @ResponseBody
    public void getCameraStream(@PathVariable String cameraId, HttpServletResponse response) {
        response.setContentType("multipart/x-mixed-replace; boundary=--BoundaryString");

        try {
            log.info("User connected. User count: {}", userCountService.fireUserConnect());

            ServletOutputStream outputStream = response.getOutputStream();

            BiConsumer<InputStream, Long> partsConsumer =
                    (inputStream, length) -> writeBoundary(outputStream, inputStream, length);

            cameraStreamService.writeCameraSnapshot(cameraId, partsConsumer);
        } catch (JustSkipMeException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.error("Error while handle camera request", e);
        } finally {
            log.info("User disconnected. User count: {}", userCountService.fireUserDisconnect());
        }
    }

    private void writeBoundary(ServletOutputStream outputStream, InputStream inputStream, long length) {
        try {
            outputStream.println("--BoundaryString");
            outputStream.println("Content-type: image/jpeg");
            outputStream.println("Content-Length: " + length);
            outputStream.println();

            IOUtils.copy(inputStream, outputStream);
            outputStream.println();
            outputStream.println();

            outputStream.flush();
        } catch (IOException e) {
            throw new JustSkipMeException("Error while write parts");
        }
    }
}
