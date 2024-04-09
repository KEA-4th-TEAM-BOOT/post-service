package com.example.postservice.repository;

import com.example.postservice.model.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.postservice.model.QPost.post;
import static com.example.postservice.model.QPostTag.postTag;
import static com.example.postservice.model.QTag.tag;

@RequiredArgsConstructor
@Repository
public class CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Post> findByTagName(String tagName, Pageable pageable) {

        List<Post> posts = queryFactory.selectFrom(post)
                .join(post.postTagList, postTag)
                .join(postTag.tag, tag)
                .where(tag.name.containsIgnoreCase(tagName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .join(post.postTagList, postTag)
                .join(postTag.tag, tag)
                .where(tag.name.containsIgnoreCase(tagName));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }
}