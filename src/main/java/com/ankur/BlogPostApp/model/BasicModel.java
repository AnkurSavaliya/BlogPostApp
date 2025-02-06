package com.ankur.BlogPostApp.model;

import com.ankur.BlogPostApp.enums.RecordStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BasicModel {

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name= "updated_date")
    @UpdateTimestamp
    private Date updatedDate;

    @Column(name = "rec_status")
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus = RecordStatus.CREATED;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }
}
