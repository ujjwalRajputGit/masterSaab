package in.mastersaab.mastersaab.dataModel;


import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class ContentData {


    @DocumentId
    private String document_id;
    private String title;
    private String content;
    private String imageUrl;
    private Date date;

    public ContentData() {}

    public ContentData(String title, String content, String imageUrl, Date date, String document_id) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.document_id = document_id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }
}
