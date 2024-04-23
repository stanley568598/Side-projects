# FreeMarker 模板引擎

## 介紹

這是一個 FreeMarker 模板引擎，能將資料填入模板 template.html，動態生成完整內容的網頁。

## Issue

- Daily life problems：依照報表格式填入資料

    - 問題描述：儘管填入一份 HTML 是簡單的工作，但當想依時間、人數...，各種資料來產生報表時，手動填入就會變成一份愚蠢的做法。
    
    - 例如：若填入資料遠少於重複資料 (背景) 時，使用 JS 填入 + 複製網頁元素，不是一個聰明的方式。

- 工具：FreeMarker 是一個基於 Java 的 模板引擎
    
    - 介紹：概念上就像國小國語課所寫的「照樣造句」，一段文字會有幾個地方被挖空，而我們要自己把詞語給填進去。

    - 用途：模板引擎是幫助我們將資料填入被挖空的模板中，產生完整的內容。

    - 優勢：針對單一用戶的需求，FreeMarker 可以在不用架設網站的情況下，直接讀取資料生成「動態網頁」。

## 功能

- [x] 加入模板【project_1\src\main\resources\templates\yyyy_template.html】
- [x] 加入資料【project_1\src\main\resources\inputs\zzzz.json】
- [x] 產生完整內容【project_1\target\output\xxxx.html】

## 檔案說明

> 此資料夾包含多個 FreeMarker 項目，每個項目的 project_1 為該專案的資料夾。

1. 使用時，請先確定整個路徑都不包含中文字，再以 VScode 開啟 project_1。 

2. 在 src 資料夾內，找到 .java 檔案，翻至 main() 即可 Run Java Program 執行程式。

> 執行檔案路徑：project_1\src\main\java\project\xxxx\XXXX.java

## 注意事項

- 請確定 Java 專案的路徑，務必不包含中文字。

    否則，執行時將會看到下列錯誤訊息。
    
    ~~~
    Error: Could not find or load main class XXXX
    Caused by: java.lang.ClassNotFoundException: XXXX
    ~~~

    - 發生原因：系統編碼設置為 UTF8 時會使得程序無法讀取輸入，導致運行 Java 無法找到 class。( 這是 Windows 系統的原生 BUG )

    - 嘗試解法：台灣 windows 10 繁體亂碼 解法
        
        > 本機系統：開始 $\to$ 設定 (齒輪) $\to$ 時間與語言 $\to$ 地區與語言 $\to$ 系統管理語言設定 $\to$
        > 
        > 非 Unicode 程式目前使用的語言 $\to$ 變更系統地區設定 $\to$ Beta: 使用 Unicode UTF-8 提供全球語言支援的勾勾 "取消" $\to$ 重開機 。
            
    - 建議解法：保持勾選【Beta: 使用 Unicode UTF-8 提供全球語言支援】，但確保 Java 專案路徑完全不包含中文字。

        - 上述嘗試解法，雖然可以使 Java 專案成功在含有中文路徑下執行，但是 terminal 的編碼設定會跑掉，導致程式內讀取/輸出中文產生亂碼問題。
        
        - 因此，建議將專案建立於無中文路徑上，使 Java 編譯正常運行，亦不影響程式本身的中文輸出。(如下路徑範例：C:\Users\USER\Downloads\FreeMarker> )

## Demo

### Generate_Html

1. template
    
    ![Generate_Html template](./assets/images/1-1.%20Generate_Html%20template.JPG)

2. input_comments
    
    ![Generate_Html input_comments](./assets/images/1-2.%20Generate_Html%20input_comments.JPG)

3. output

    ![Generate_Html output](./assets/images/1-3.%20Generate_Html%20output.JPG)


### Generate_Receipt

0. parameters

    ![Generate_Receipt parameters](./assets/images/2-0.%20Generate_Receipt%20parameters.JPG)

1. template

    ![Generate_Receipt template](./assets/images/2-1.%20Generate_Receipt%20template.JPG)

2. input_students

    ![Generate_Receipt input_students](./assets/images/2-2.%20Generate_Receipt%20input_students.JPG)

3. outputs

    ![Generate_Receipt output-1](./assets/images/2-3.%20Generate_Receipt%20output-1.JPG)
    
    ![Generate_Receipt output-2](./assets/images/2-3.%20Generate_Receipt%20output-2.JPG)
    
    ![Generate_Receipt output-3](./assets/images/2-3.%20Generate_Receipt%20output-3.JPG)
