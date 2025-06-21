package com.posterr_backend.dtos;

public class PostRequest {
    private String content;
    private Long originalPostId;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getOriginalPostId() { return originalPostId; }
    public void setOriginalPostId(Long originalPostId) { this.originalPostId = originalPostId; }
}
