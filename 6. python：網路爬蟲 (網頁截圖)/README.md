# python 網路爬蟲：網頁截圖

## 介紹

這是一個自動化網頁截圖的爬蟲程式。

## Issue

- Daily life problems：連續擷取整個網頁文件畫面

    - 問題描述：有些網路資源 ( 如 SlideShare、HackMD )，提供的下載功能不完全。如：僅開放付費下載、文件分割在多個網頁裡、資料內容可能被拆分在下載文件的兩頁之間、只存取文字內容，未包含完整 css 渲染效果。

    - 目標：使用連續畫面截圖，按照固定的規格保存資料。

## 功能

- [x] 開啟目標網頁
- [x] 跳轉網頁 / 下拉網頁
- [x] 固定截圖比例

## 檔案說明

> 本專案分成兩個系列，分別處理水平資料、垂直資料。

1. snapshot

    - 水平資料：每一張投影片都儲存在一個單獨的網址中，但網站未提供下一頁的跳轉按鍵，並且需要一張一張右鍵另存圖片。
    
    - 輸出檔案：爬蟲程式「web_crawler ( chrome_snapshot ).py」，按照順序逐一截圖到【./snapshot/slides】的資料夾之中。
    
    - 人工部分：可以透過 PDFsam 等工具，將多張截圖合併 ( merge ) 成一整個文件。

2. full_screen_snapshot
    
    - 垂直資料：大部分網站的內容都向下延伸，但存檔時容易發生內容卡在兩頁之間的問題。
    
    - 輸出檔案：爬蟲程式「web_crawler ( chrome_full_screen_snapshot ).py」，按照截圖大小下拉網頁，逐漸將完整的畫面截圖下來，再將其串聯成連續的長截圖，儲存在【./full_screen_snapshot】的資料夾之中。
    
    - 人工部分：可以透過 excel 調成整頁模式，再用分頁符號在適合的地方進行換頁，而不切到連續的資料內容 。

## Demo

1. snapshot
    
    ![snapshot](./assets/images/1.%20snapshot.JPG)

2. full_screen snapshot
    
    ![full_screen snapshot](./assets/images/2.%20full_screen%20snapshot.JPG)
