package edu.du.review.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawl")
public class BlogCrawlController {

    @GetMapping
    public ResponseEntity<String> crawlBlog(@RequestParam String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();

            Element frame = doc.selectFirst("iframe#mainFrame");

            if (frame != null) {
                String frameSrc = frame.attr("src");
                String fullFrameUrl = "https://blog.naver.com" + frameSrc;

                Document innerDoc = Jsoup.connect(fullFrameUrl)
                        .userAgent("Mozilla/5.0")
                        .get();

                String title = innerDoc.title();
                String content = innerDoc.select("div.se-main-container").text();

                if (content.isBlank()) {
                    content = innerDoc.select("div#postViewArea").text(); // 구버전 블로그
                }
                return ResponseEntity.ok(title + "\n\n" + content);
            } else {
                return ResponseEntity.ok("iframe을 찾을 수 없습니다. 이 블로그는 형식이 다를 수 있습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("크롤링 실패: " + e.getMessage());
        }
    }
}
