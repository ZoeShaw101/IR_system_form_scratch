# IR system

基于基本向量空间模型，先用爬虫爬取新闻组成语料库，使用[Hanlp分词库](https://github.com/hankcs/HanLP)进行分词,再使用TF-IDF算法，目前已完成query和document相似度矩阵的计算。
***
## 目前效果
![](http://wx3.sinaimg.cn/mw690/a7e8df1dly1fhqme0x09rj210r0jy0ya.jpg)
***
## to-do
- [x] python爬虫爬取新闻网站
- [x] 文档分词，分词清理；
- [x] TF-IDF（term frequency–inverse document frequency）
- [ ] 实现倒排索引算法
- [ ] 在网页前端展示
- [ ] 信息抽取
