#!/usr/bin/env python3

import os
import threading
import re
from bs4 import BeautifulSoup
from urllib.request import urlopen
from selenium import webdriver

browserPath = '/opt/phantomjs-2.1.1-linux-x86_64/bin/phantomjs'
#homePage = 'http://jingji.cctv.com/'
homePage='http://news.cctv.com/tech/index.shtml'
outputDir = 'newslist/'
parser = 'html5lib'


def main():
    driver = webdriver.PhantomJS(executable_path=browserPath)  #浏览器的地址
    driver.get(homePage)  #访问目标网页地址
    bsObj = BeautifulSoup(driver.page_source, parser)  #解析目标网页的 Html 源码
    #print("[*]OK GET Page")
    #newslist=bsObj.find_all("a",{"href":re.compile("[http]{4}:\/\/[jingji]{6}\.[cctv]{4}\.[com]{3}\/2017\/[0-9]{2}\/[0-9]{2}\/[a-zA-Z0-9]{30}\.shtml")})
    newslist=bsObj.find_all("a",{"href":re.compile("[http]{4}:\/\/[news]{4}\.[cctv]{4}\.[com]{3}\/2017\/[0-9]{2}\/[0-9]{2}\/[a-zA-Z0-9]{30}\.shtml")})
    print("得到文章网址列表")
    for news in newslist:
        if news.text is not "":
            artextrac(news["href"],news.text)

    driver.close()

def artextrac(url,name):
    driver = webdriver.PhantomJS(executable_path=browserPath)  # 浏览器的地址
    driver.get(url) # 访问目标网页地址
    print(url)
    bsObj = BeautifulSoup(driver.page_source, parser)
    artlay=bsObj.find("div",{"class":"cnt_bd"})
    print(artlay)
    artcont=artlay.findAll("p")
    print(artcont)
    strcon=""
    for artc in artcont:
        strcon+=artc.text
    with open(outputDir + name+".txt", 'w') as f:
        f.write(strcon)
    print("写入文章")
    driver.close()

main()