from selenium import webdriver
from selenium.webdriver.common.by import By

from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

from PIL import Image

import time
import math
import os

url='https://hackmd.io/c0IIraoBROSWbQ3AOf4KkA'

# 放大畫面，提升足夠的解析度。
scale = 2

# 填充目標區域
padding = 3

# 等候幾秒，使畫面完全載入完成
delay = 10

# Set the path where the screenshot will be saved
path = os.path.dirname(os.path.abspath(__file__))

# Configure WebDriver options
options = Options()
options.add_argument("--start-maximized")
options.add_argument("--headless")          # Use headless mode for running in the background
options.add_argument("--disable-gpu")
options.add_argument("--window-size=1920,1080")

# # 在 headless 背景運行模式下，UI 縮放不適用。
# options.add_argument("--ash-enable-scale-settings-tray")
# options.add_argument("--force-device-scale-factor=" + str(scale))
# options.add_argument("--high-dpi-support=" + str(scale))

driver = webdriver.Chrome(ChromeDriverManager().install(), options = options)

# 視窗最大化
driver.maximize_window()

# We go to the webpage here and wait for fully load
driver.get(url)
time.sleep(delay)

# Getting current URL source code 
get_title = driver.title

print("\n" , "title:", get_title , "\n" )

# =============================================================================

# # 直接全圖下載，會因大小問題，使圖片細節失真。

# # Use JavaScript to get the full width and height of the webpage
# width = driver.execute_script("return Math.max( document.body.scrollWidth, document.body.offsetWidth, document.documentElement.clientWidth, document.documentElement.scrollWidth, document.documentElement.offsetWidth );")
# height = driver.execute_script("return Math.max( document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight );")

# # Set the window size to match the entire webpage
# driver.set_window_size(width, height)
# time.sleep(delay)

# # Find the full page element (usually 'body') and capture the screenshot
# full_page = driver.find_element(by=By.ID, value="doc")
# full_page.screenshot(f"full_screen_snapshot/" + get_title + ".png")

# =============================================================================

# 在無頭模式下，縮放內容
driver.execute_script('document.body.style.zoom = ' + str(scale))

# 解決圖片，放大縮小時模糊失真的問題，添加 image-rendering
driver.execute_script("var styleSheet = document.createElement('style'); styleSheet.innerText = 'html { image-rendering: -webkit-optimize-contrast;  image-rendering: crisp-edges; }'; document.head.appendChild(styleSheet);")

# 隱藏多餘的元件
driver.execute_script('document.getElementsByTagName("nav")[0].style.display="none";')
driver.execute_script('document.getElementsByClassName("ui-infobar container-fluid unselectable hidden-print comment-enabled")[0].style.display="none";')
driver.execute_script('document.getElementById("ui-toc-affix").style.display="none";')

# 填充更多空白，使得文件尾部為連續截圖。
driver.execute_script('div = document.createElement("div"); div.style.height = "1080px"; var doc = document.getElementById("doc"); doc.appendChild(div);')
time.sleep(delay)

# 如果元素被 overflow 擋住，scroll 取得全高
scroll_height = driver.execute_script('return document.documentElement.scrollHeight')
window_height = driver.execute_script('return window.innerHeight')                      # 載入視窗的高度
num = int( math.ceil( float(scroll_height) / float(window_height) ) )

# 目標定位
element_x = int( driver.execute_script('return document.getElementById("doc").offsetLeft') * scale )
element_width = int( driver.execute_script('return document.getElementById("doc").offsetWidth') * scale )
target_x = element_x - padding;
target_width = element_width  + target_x + padding * 4;

# get temp files
tempfiles = []
for i in range( num ):
    path = f"./full_screen_snapshot/selenium_screenshot-" + str(i+1) + ".png"
    file = open(path, 'w+')
    file.close()
    tempfiles.append(path)

remove_files = tempfiles

try:
    # take screenshots
    for i, path in enumerate(tempfiles):
        
        if i > 0:
            driver.execute_script( 'window.scrollBy(%d,%d)' % (0, window_height) )
            time.sleep(delay)
        
        driver.save_screenshot(path)

        # 切出目標區域
        im = Image.open(path)

        im = im.crop((target_x, 0, target_width, window_height))

        im.save(path)

        print(i, end=" ")

    print("\n" , "Done: screenshot")

    # ==============================================================

    # 縫合超長圖片，可能使後續難以操作。

    # # stitch images together
    # stiched = Image.new('RGB', (target_width, scroll_height))

    # for i, path in enumerate(tempfiles):
        
    #     img = Image.open(path)
            
    #     w, h = img.size
    #     y = window_height * i
            
    #     if i == ( len(tempfiles) - 1 ):
    #         img = img.crop((0, h-(scroll_height % h), w, h))
    #         w, h = img.size
            
    #     stiched.paste(img, (
    #         0,      # x0
    #         y,      # y0
    #         w,      # x1
    #         y + h   # y1
    #     ))
    #     img.close()

    # stiched.save("full_screen_snapshot\\" + get_title + ".png")

    # ==============================================================

    pieces = 50

    part = math.ceil(num / pieces)

    # 瀏覽網頁時，可能有資訊刷新(數學式)，使得最終長度與一開始的不同。
    update_scroll_height = driver.execute_script('return document.documentElement.scrollHeight')
    
    last_length = (update_scroll_height % window_height)

    for p in range(part):

        if len(tempfiles) < pieces * (p+1):
            stich_files = tempfiles
        else:
            stich_files = tempfiles[ : pieces * (p+1) ]
            tempfiles = tempfiles[ pieces * (p+1) : ]

        length = len(stich_files)

        # stitch images together
        stiched = None

        if len(stich_files) < pieces:
            stiched = Image.new('RGB', (target_width - target_x, window_height * (length - 1) + last_length))
        else:            
            stiched = Image.new('RGB', (target_width - target_x, window_height * length))

        for i, path in enumerate(stich_files):
            
            img = Image.open(path)
                
            w, h = img.size
            y = window_height * i
                
            # <畫面滑到底部，最後一張，上方會有重複的部分>：靠填充空白避免截圖出錯。
            if (len(stich_files) < pieces) and (i == ( len(stich_files) - 1 )):
                img = img.crop((0, h - last_length, w, h))
                w, h = img.size

            stiched.paste(img, (
                0,      # x0
                y,      # y0
                w,      # x1
                y + h   # y1
            ))
            img.close()

        stiched.save("full_screen_snapshot\\" + get_title + "_" + str(p+1) + ".png")
        print("\n" , "Output: full_screen_snapshot\\" + get_title + "_" + str(p+1) + ".png")

    print("\n" , "Done: stitch images")

finally:
    # cleanup
    for path in remove_files:
        if os.path.isfile(path):
            os.remove(path)

print("\n" , "Done all: success", "\n")

# Close the driver window
driver.quit()
