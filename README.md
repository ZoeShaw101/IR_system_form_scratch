# IR system
先用爬虫爬取新闻组成语料库，使用[Hanlp分词库](https://github.com/hankcs/HanLP)进行分词,再使用TF-IDF算法，目前已完成query和document相似度矩阵的计算。
## 文件结构
基于intellij idea 构建项目

IR
|--Docs:语料库
|--src: 源代码

## to-do
- [x] TF-IDF（term frequency–inverse document frequency）
- [] 实时爬虫 
- [] 在网页展示
