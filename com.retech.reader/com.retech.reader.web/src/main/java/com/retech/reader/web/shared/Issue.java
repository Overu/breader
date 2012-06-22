package com.retech.reader.web.shared;

import com.goodow.web.core.shared.WebEntity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Issue extends WebEntity implements HasResource {

  @NotNull(message = "你必须指定一个名称")
  @Size(min = 3, message = "名称不得少于3个字符")
  private String title;
  @ManyToOne(cascade = CascadeType.ALL)
  private Category category;

  private long viewCount;
  private Date createTime;

  private Date updateTime;

  @OneToOne(cascade = CascadeType.PERSIST)
  private Resource image;

  @Lob
  private String detail;

  @OneToMany(mappedBy = "issue")
  private List<Section> sections;

  @Override
  public void becomeLightWeight() {
    if (image != null) {
      image.becomeLightWeight();
    }
  }

  public Category getCategory() {
    return category;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public String getDetail() {
    return detail;
  }

  public Resource getImage() {
    return image;
  }

  public List<Section> getSections() {
    return sections;
  }

  public String getTitle() {
    return title;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public long getViewCount() {
    return viewCount;
  }

  public Issue setCategory(final Category category) {
    this.category = category;
    return this;
  }

  public Issue setCreateTime(final Date createTime) {
    this.createTime = createTime;
    return this;
  }

  public Issue setDetail(final String detail) {
    this.detail = detail;
    return this;
  }

  public Issue setImage(final Resource image) {
    this.image = image;
    return this;
  }

  public Issue setSections(final List<Section> sections) {
    this.sections = sections;
    return this;
  }

  public Issue setTitle(final String title) {
    this.title = title;
    return this;
  }

  public Issue setUpdateTime(final Date updateTime) {
    this.updateTime = updateTime;
    return this;
  }

  public Issue setViewCount(final long viewCount) {
    this.viewCount = viewCount;
    return this;
  }

}
