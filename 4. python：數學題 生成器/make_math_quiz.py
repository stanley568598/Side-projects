import random
import time
import os
import errno
 
def get_num():
    return str(random.randint(10, 200))

def get_operator():
    select = random.randint(0, 3)
    if select == 0:
        return '+'
    elif select == 1:
        return '-'
    elif select == 2:
        return '*'
        # return 'x'
    elif select == 3:
        return '/'
        # return '÷'

def get_time():
    now = time.time()
    result = time.localtime(now)
    """
    print("年：", result.tm_year)
    print("月：", result.tm_mon)
    print("日：", result.tm_mday)
    print("時：", result.tm_hour)
    print("分：", result.tm_min)
    print("秒：", result.tm_sec)
    """
    return str(result.tm_year) + '年 ' + str(result.tm_mon) + '月 ' + str(result.tm_mday)  + '日 - ' + str(result.tm_hour) + '時 ' + str(result.tm_min) + '分 ' + str(result.tm_sec) + '秒'

def make_file(doc_set, ans_set):

    time_str = get_time()
    
    f1_name = "./quiz/quiz - " + time_str + ".txt"
    f2_name = "./answer/ans - " + time_str + ".txt"

    if not os.path.exists(os.path.dirname(f1_name)):
        try:
            os.makedirs(os.path.dirname(f1_name))
        except OSError as exc: # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise

    if not os.path.exists(os.path.dirname(f2_name)):
        try:
            os.makedirs(os.path.dirname(f2_name))
        except OSError as exc: # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise

    # 開啟檔案
    f1 = open(f1_name, "w+")
    f2 = open(f2_name, "w+")
    
    for q in range(int(len(doc_set))):
        f1.write("第 " + str(q + 1) + " 題：")
        f2.write("第 " + str(q + 1) + " 題：")

        doc_set[q].replace('+', ' + ')
        doc_set[q].replace('-', ' - ')
        doc_set[q].replace('*', ' x ')
        doc_set[q].replace('/', ' ÷ ')

        f1.write(doc_set[q] + " = \n\n")
        f2.write(doc_set[q] + " = " + str(ans_set[q]) + ". \n\n")
    
    # 關閉檔案
    f1.close()
    f2.close()

def main():
    doc_set = []
    ans_set = []
    while int(len(doc_set)) != 20:
        string = ''
        for j in range(5):
            if j % 2 == 0:
                string += get_num()
            elif j % 2 == 1:
                string += get_operator()

        try:
            ans = eval( string )
        except:
            ans = 1.5
            pass

        if type(ans) == int and ans <= 1000 and ans > 0:
            doc_set.append(string)
            ans_set.append(ans)
    
    # print(doc_set)
    # print(ans_set)

    make_file(doc_set, ans_set)

if __name__ == '__main__':
    main()
