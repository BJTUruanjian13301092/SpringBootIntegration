package com.example.spring_boot_test.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.List;
import java.util.Properties;

@Service
public class StandfordNLPService {

    public void testMyNlp(){

        Properties props = PropertiesUtils.asProperties(
                "annotators" , "segment, ssplit, pos, ner",
                "customAnnotatorClass.segment" , "edu.stanford.nlp.pipeline.ChineseSegmenterAnnotator",
                "segment.model" , "edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
                "segment.sighanCorporaDict" , "edu/stanford/nlp/models/segmenter/chinese",
                "segment.serDictionary" , "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
                "segment.sighanPostProcessing" , "true",
                "ssplit.boundaryTokenRegex" , "[.]|[!?]+|[。]|[！？]+",
                "pos.model" , "edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger",
                "ner.model" , "edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz",
                "ner.applyNumericClassifiers" , "false",
                "ner.useSUTime" , "false");

//        Properties props = PropertiesUtils.asProperties("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");

        StanfordCoreNLP standFordNlp = new StanfordCoreNLP(props);
        //Annotation annotation = new Annotation("大家好我是渣渣辉,一起来玩贪玩蓝月");
        Annotation annotation = new Annotation("政府工作报告指出，2017年的经济稳步增长");

        standFordNlp.annotate(annotation);

        //根据标点符号，进行句子的切分，每一个句子被转化为一个CoreMap的数据结构，保存了句子的信息()
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        //从CoreMap 中取出CoreLabel List ,打印
        for (CoreMap sentence : sentences){
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){
                String word = token.get(CoreAnnotations.TextAnnotation.class);
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                System.out.println(word);
//                System.out.println(pos);
//                System.out.println(ne);
            }
        }
    }

}
