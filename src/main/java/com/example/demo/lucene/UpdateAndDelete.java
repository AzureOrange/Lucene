package com.example.demo.lucene;

import com.example.demo.domain.User;
import com.example.demo.util.LuceneUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 更新和删除
 *
 * @author orange
 * @Time 2018/9/12 0012
 */
@Slf4j
@Component
public class UpdateAndDelete {

    /**
     * 更新Lucene失败
     *
     * @param user
     * @throws Exception
     */
    public void updateIndexDB(User user) throws Exception {
        IndexWriter writer = null;
        try {
            Document document = LuceneUtil.javabean2document(user);
            Term term = new Term("id", user.getId());

            IndexWriterConfig config = new IndexWriterConfig(LuceneUtil.getStandardAnalyzer());

            writer = new IndexWriter(LuceneUtil.getDirectory(), config);
            writer.updateDocument(term, document);
        } catch (Exception e) {
            log.error("更新Lucene失败");
            throw new Exception();
        } finally {
            //关闭writer
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除Lucene指定记录
     *
     * @param id
     * @throws Exception
     */
    public void deleteIndexDB(String id) throws Exception {
        IndexWriter writer = null;
        try {
            Term term = new Term("id", id);

            IndexWriterConfig config = new IndexWriterConfig(LuceneUtil.getStandardAnalyzer());

            writer = new IndexWriter(LuceneUtil.getDirectory(), config);
            writer.deleteDocuments(term);
        } catch (Exception e) {
            log.error("删除Lucene失败");
            throw new Exception();
        } finally {
            //关闭writer
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
