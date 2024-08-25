package com.example.webfluxplayground.test.sec02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = {
        "sec=sec02"/*,
        "logging.level.org.springframework.r2dbc=DEBUG",*/
})
public abstract class AbstractTest {
}
