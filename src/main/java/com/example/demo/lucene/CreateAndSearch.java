package com.example.demo.lucene;

import com.example.demo.domain.User;
import com.example.demo.util.LuceneUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * descript
 *
 * @author orange
 * @Time 2018/9/11 0011
 */
@Slf4j
@Component
public class CreateAndSearch {

    public void createIndex(List<User> users) throws Exception {
        IndexWriter writer = null;
        try {
            Directory directory = LuceneUtil.getDirectory();

            IndexWriterConfig config = new IndexWriterConfig(LuceneUtil.getStandardAnalyzer());

            writer = new IndexWriter(directory, config);

            // 清除以前的index
            writer.deleteAll();

            for (User o : users) {
                Document doc = LuceneUtil.javabean2document(o);

                writer.addDocument(doc);
            }
        } catch (Exception e) {
            log.error("Lucene创建失败");
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
     * 搜索
     *
     * @param keyWord
     * @return
     */
    public List<User> search(String keyWord) {
        List<User> users = new ArrayList<>();
        try {
            //1.创建Directory
            Directory directory = LuceneUtil.getDirectory();

            //2.创建IndexSearcher检索索引的对象，里面要传递上面写入的内存目录对象directory
            DirectoryReader ireader = DirectoryReader.open(directory);
            //3.指向索引目录的搜索器
            IndexSearcher indexSearcher = new IndexSearcher(ireader);

            // 4、创建搜索的Query
            Analyzer analyzer = new StandardAnalyzer();
            //Analyzer analyzer = new IKAnalyzer(true); // 使用IK分词

            // 简单的查询，创建Query表示搜索域为content包含keyWord的文档
            //Query query = new QueryParser("content", analyzer).parse(keyWord);
            String[] fields = {"id", "age", "content"};
            // MUST 表示and，MUST_NOT 表示not ，SHOULD表示or
            BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
            // MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国"
            Query multiFieldQuery = MultiFieldQueryParser.parse(keyWord, fields, clauses, analyzer);

            // 5、根据searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(multiFieldQuery, 100); // 搜索前100条结果
            System.out.println("共找到匹配处：" + topDocs.totalHits);
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("共找到匹配文档数：" + scoreDocs.length);

            QueryScorer scorer = new QueryScorer(multiFieldQuery, "content");
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"backgroud:red\">", "</span>");
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                String content = document.get("content");

                // 8、根据Document对象获取需要的值
                User user = (User) LuceneUtil.document2javabean(document, User.class);
                user.setContent(highlighter.getBestFragment(analyzer, "content", content));
                users.add(user);
            }

            ireader.close();
            directory.close();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return users;
        }
    }
}
