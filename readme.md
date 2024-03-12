# BMI 計算機

## 程式架構

本程式採用MVVM架構夠進行開發，將UI層和資料層分離，增加可讀性和可維護性。

## 應用程式畫面＆功能介紹

### 主要畫面

![image](/assets/main_screen.png)

亮點：

1. 使用 `Card` Compose 元件製作卡片效果
2. 性別選擇用 `SingleChoiceSegmentedButtonRow` 進行開發
3. 文字輸入框使用 `OutlinedTextField`，並撰寫一個 `MyOutlinedTextField` 元件方便相同樣式的元件重複使用
4. UI 整體設計遵循 Material 3（盡力，美感不太好）

### 輸入資料畫面

![image](/assets/text_input_screen.png)
亮點：

1. `OutlinedTextField` 內建效果會將 placeholder 往上移動（含動畫）
2. 鍵盤為數字鍵盤

### 輸入資料畫面（錯誤）

![image](/assets/error_message_screen.png)
使用 `Toast` 顯示錯誤訊息

### 結果顯示畫面

![image](/assets/result1.png)
![image](/assets/result2.png)
![image](/assets/result3.png)
根據使用者輸入的數值進行計算，將結果顯示在 `AlertDialog` 並給予不同圖片回饋。
