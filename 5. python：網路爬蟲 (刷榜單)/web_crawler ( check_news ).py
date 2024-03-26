import requests
from bs4 import BeautifulSoup
import json
import time
from datetime import datetime
from datetime import timedelta
import re

import warnings

warnings.filterwarnings("ignore")


def my_request(url):
    
    try:
        res = requests.post(url, verify = False)

    except requests.exceptions.HTTPError as errh:
        raise SystemExit("Http Error:", errh)
    
    except requests.exceptions.ConnectionError as errc:
        raise SystemExit("Error Connecting:", errc)
    
    except requests.exceptions.Timeout as errt:
        raise SystemExit("Timeout Error:", errt)
    
    except requests.exceptions.RequestException as err:
        raise SystemExit("OOps: Something Else", err)
    
    return res

print("\n===========\n")

# 輸入年度
year = 113
school = "台灣聯合大學系統{0}學年".format(year)

while True :
    
    respond = my_request('https://exam.nycu.edu.tw/')

    respond.encoding = 'utf-8'

    soup = BeautifulSoup(respond.text)
    # print(soup)

    getIframe = soup.find('iframe', { 'id': 'myIframe' })     # 抓到 最新消息
    # print(getIframe)
    # print(getIframe["src"])
    
    getNews = my_request(getIframe["src"])

    New_soup = BeautifulSoup(getNews.text)

    getAllNew = New_soup.find('table', { 'id': 'dlCateL' })     # 抓到新聞的 table
    # print(getAllNew)
    
    t1 = timedelta(hours = 8)
    timenow = (datetime.now() + t1).strftime("%m/%d/%Y, %H:%M:%S")

    print('\r%s' % timenow, end = " ") 

    # print(len(getAllNew.find_all('a')))

    if len(getAllNew.find_all('a')) > 5 : 

        release = False

        for link in getAllNew.find_all('a'):
        
            if school in link.text:

                if release == False: 
                    print('| 已放榜')

                # print(link)
                
                title = link.get('title')

                print('\n - ', title, end = " ") 

                onclick = link.get('onclick')
                # print(onclick)

                id = re.findall("(?<='action\.aspx\?)([^']*)", onclick)
                # print(id)
                # print(id[0])

                url = "https://sr2.aa.nycu.edu.tw/popup.aspx?type=1&" + id[0]
                print(url)

                release = True

        if release == False: 

            print('| 未放榜')
            
            time.sleep(5)  # 等 10秒 刷一次網站：爬太多次會被擋要小心使用。
        
        else:
            
            print("\n==== END ====\n")

            break

    else : 

        print("| 放榜連結 尚未出現，請再耐心等候！")
        
        time.sleep(30)  # 等 30秒 刷一次網站：爬太多次會被擋要小心使用。
    
    # print("\n===========\n")
        
# 請用 ctrl + c 手動結束 無限迴圈
