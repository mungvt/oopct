# OOP Extra Tasks
Project for oop subject.

## Task 1: 
### Question:
Dear all
Chúng ta có thêm 1 bài tập bổ sung để tạo cơ hội cộng điểm cho các bạn
Các em viết bài tập Java thực hiện chức năng sau:
1) Đọc file vd012.net.xml
2) Cho phép người dùng nhập vào một mã và kiểm tra trong các lane của file, có lane nào trùng id với mã đã nhập không? Nếu có thì in ra màn hình id của edge chứa lane đó, index của lane và chiều dài của lane.
Chẳng hạn nếu nhập mã "E0_1" thì sẽ tìm ra được dòng sau trong XML:

```
    <edge id="E0" from="J2" to="J3" priority="-1">

        <lane id="E0_0" index="0" allow="pedestrian" speed="13.89" length="9.03" width="2.00" shape="-12.50,8.30 -3.48,8.30"/>

        <lane id="E0_1" index="1" disallow="pedestrian" speed="13.89" length="9.03" shape="-12.50,10.90 -3.47,10.90"/>

    </edge>
```

id của edge là E0, index của lane là 1 và chiều dài của lane là 9.03

3) cho người dùng nhập vào id của một lane và tìm ra tất cả các lane có thể đi đến từ lane đó, sau đó ghi ra file output.txt
Chương trình sẽ ghi ra các dòng với định dạng
```
    <Id của lane>   <chiều dài lane>  <lane kết nối 1> ... <lane kết nối n>
```
Chẳng hạn 
Nếu nhập id là "-E1_1" thì sẽ ghi ra được bốn dòng đầu là:
```
    -E1_1 5.3 E1_1 -E0_1
    -E0_1 9.05 E0_1
    E0_1 9.03 E1_1 -E0_1
    E1_1 5.30 -E1_1 -E299_1 E2_1
```

Giải thuật tìm các lane có kết nối với một lane như sau:

- input: id của lane, file vd012.net.xml (vd: id là -E1_1)
- bước 1: tìm id của edge chứa lane, chiều dài lane và index của lane (sẽ ra được id của edge là -E1 và chiều dài 5.3, index của lane là 1)
- bước 2: tìm trong các thẻ connection, thẻ nào có from là id của edge và fromLane có giá trị bằng index của lane. Nếu có thẻ đó, lấy ra giá trị của thuộc tính to và thuộc tính toLane
(sẽ ra được connection nối -E1 với -E0, connection này cũng có fromLane là 1 - trùng với index của -E1_1 là 1. Khi ấy thuộc tính to là -E0 và toLane là 1)
- bước 3: xác định được lane thuộc edge có id trùng với thuộc tính to (ở trên) và index trùng với toLane (ở trên). Ta có thể thấy tìm được lane nối với -E1_1 là -E0_1
- bước 4: quay lại với bước 2 nếu có nhiều hơn 1 connection ở bước đó. Nếu không còn connection nào thì quay lại bước 1 với id của lane là id tìm được trong bước 3.

### Answer: How to run
- File input: vd012.net.xml
- File answer: src/FindRouteJob.java
- Run this file and do following the program.
- File output: output.txt
