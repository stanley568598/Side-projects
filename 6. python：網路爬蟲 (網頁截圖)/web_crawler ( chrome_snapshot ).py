from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
import time
import os

url_1='https://image.slidesharecdn.com/pjr2talkforslideshare-180504134358/95/extending-rsquared-beyond-ordinary-leastsquares-linear-regression-'
url_page = str(1)
url_2='-1024.jpg?cb=1525441501%201024w'

driver = webdriver.Chrome(ChromeDriverManager().install())
driver.set_window_size(1200,4500)                           # 此步驟為關鍵，設定成整個頁面的長度大小

file_name = "slides"

for i in range(34):
    url_page = str(i + 1)
    print("page:" , url_page)
    
    url = url_1 + url_page + url_2
    driver.get(url)

    path = './snapshot/' + file_name + '/'
    if not os.path.isdir(path):
        os.makedirs(path)
    
    driver.get_screenshot_as_file(path + url_page + '.jpg')
    # driver.close()