# FreeMarker

此資料夾包含多個 FreeMarker 項目，每個項目的 project_1 為專案的資料夾。

使用時，以 vscode 單獨開啟一個專案 (在專案項目的資料夾內，通常以 project_1 命名)。 

在 src 資料夾內，找到 .java 檔案，翻至 main() 可以 Run Java Program。

## 注意事項

<font color="red">請確定 Java 專案的路徑，務必不包含中文字。</font>

否則，將會看到下列錯誤訊息。

    Error: Could not find or load main class XXXX
    Caused by: java.lang.ClassNotFoundException: XXXX

> [!WARNING]
> 問題原因：
> 系統編碼設置為 UTF8 時會使得程序無法讀取輸入，導致運行 Java 無法找到 class。(這是 Windows 的 BUG，目前已知能不受此限的語言為 Python。)
> 常見解法，不能啟用那個控制面板的選項(如下詳細步驟)，也不能使用 chcp 65001。系統編碼一定只能是默認的 936 (GBK)

> [!NOTE]
> 嘗試解法：台灣 windows 10 繁體亂碼 解法
> 本機系統：開始 $\to$ 設定 (齒輪) $\to$ 時間與語言 $\to$ 地區與語言 $\to$ 系統管理語言設定 $\to$
> 非 Unicode 程式目前使用的語言 $\to$ 變更系統地區設定 $\to$ Beta: 使用 Unicode UTF-8 提供全球語言支援的勾勾 "取消" $\to$ 重開機 。

> [!IMPORTANT]
> 建議做法：保持勾選 Beta: 使用 Unicode UTF-8 提供全球語言支援，但確保 Java 專案路徑完全不包含中文字。
> 上述嘗試解法，雖然可以使 Java 專案成功在含有中文路徑下執行，但是 terminal 的編碼設定會跑掉，導致程式內讀取/輸出中文產生亂碼問題。
> 因此，建議將專案建立於無中文路徑上，使 Java 編譯正常運行，亦不影響程式本身的中文輸出。(如下路徑範例：C:\Users\USER\Downloads\FreeMarker> )
