package com.merkle.wechat.vo.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.merkle.wechat.common.entity.Tag;

public class TagStatisticsVo extends StatisticsPage {
    private List<String> tagNames = new ArrayList<>();
    private List<Long> tagFollowerCounts = new ArrayList<>();

    public TagStatisticsVo(Page<Tag> page) {
        super(page);
        List<Tag> tags = page.getContent();
        tags.forEach((tag) -> {
            tagNames.add(tag.getName());
            tagFollowerCounts.add(tag.getCount());
        });
    }

    public TagStatisticsVo() {
        super();
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public List<Long> getTagFollowerCounts() {
        return tagFollowerCounts;
    }

    public void setTagFollowerCounts(List<Long> tagFollowerCounts) {
        this.tagFollowerCounts = tagFollowerCounts;
    }

}
