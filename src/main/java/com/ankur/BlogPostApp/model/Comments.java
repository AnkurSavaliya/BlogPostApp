package com.ankur.BlogPostApp.model;



import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comments")
@Where(clause = "rec_status<>'DELETED'")
public class Comments extends BasicModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_text", length = 255)
    private String commentText;

    @Column(name = "blog_id")
    private Long blogId;

    @ManyToOne(targetEntity = Blog.class)
    @JoinColumn(name = "blog_id", insertable = false, updatable = false)
    private Blog blog;

    @Column(name = "creator_id")
    private Long creatorId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private User creatorUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public User getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }
}
