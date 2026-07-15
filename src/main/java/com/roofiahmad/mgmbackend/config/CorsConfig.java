package com.roofiahmad.mgmbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Mengizinkan semua path URL (termasuk /appeals dan /intranet/**)
                        .allowedOrigins("*") // Mengizinkan semua domain asal (frontend). Ganti dengan URL frontend spesifik di production (misal: "http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Mengizinkan method HTTP apa saja
                        .allowedHeaders("*") // Mengizinkan semua HTTP Headers
                        .exposedHeaders("Authorization") // Mengekspos header tertentu jika kamu pakai JWT nantinya
                        .maxAge(3600); // Durasi browser menyimpan respon pre-flight CORS (dalam detik)
            }
        };
    }
}
