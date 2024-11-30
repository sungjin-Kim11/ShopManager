# 물건 관리 앱

---

## 구성 화면
![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)

---

## ID 설정
fragment_item_list.xml
- toolbarItemList
- recyclerViewItemList
- fabItemListAdd

fragment_add_item.xml
- toolbarAddItem
- textInputLayoutName
- textInputLayoutPrice
- textInputLayoutAbout
- toggleInputSellingOrSold
- buttonInputSelling
- buttonInputSold
- imageAddView
- buttonInputSearchphoto

fragment_read_item.xml
- toolbarReadItem
- textFieldReadItemName
- textFieldReadItemPrice
- textFieldReadItemAbout

fragment_modify_item.xml
- toolbarModifyItem
- textModifyLayoutName
- textModifyLayoutPrice
- textModifyLayoutAbout
- toggleModifySellingOrSold
- buttonModifySelling
- buttonModifySold
- imageModifyView
- buttonModifySearchphoto

toolbar_add_item_menu
- toolbar_add_item_check

toolbar_read_item_menu
- toolbar_read_menu_item_delete
- toolbar_read_menu_item_delete

---

## 구현 해야할 기능
- 물건 관리(등록, 수정, 삭제, 물건 검색 등등)
- 물건의 사진을 등록할 수 있게 만들어 주면 기본 30점짜리
- 사진 데이터는 파일 입출력을 통해 저장
- 물건 데이터는 SQLiteDatabase에 저장한다.  -> 20점짜리
- RoomDatabase를 사용 -> 30점짜리
- 어떠한 기능들을 구현하였는지 -> 10점
- 화면 구성 -> 10점

---

## 팀 기본 구성

### MainFragment
- Toolbar -> Drawer
- TextField (검색 기능)
- RecycleView
- FAB 버튼 -> 추가

### DrawerLayout
- 전체 물품
- 판매중
- 판매완료 -> filter tkdyd

### InputFragment
- Toolbar (뒤로가기, 저장)
- scrollView 사용
- TextField (상품 명)
- TextField (상품 가격)
- TextField (상품 설명)
- ImageView (상품 사진 보여주기)
- Button(사진 찾아보기)

### ShowFragment
- Toolbar (뒤로가기, 수정, 삭제)
- scrollView 사용
- ImageView (상품 이미지)
- TextView (상품 명)
- TextView (상품 가격)
- TextView (상품 설명)

### ModifyFragment
- Toolbar (뒤로가기, 저장)
- scrollView 사용
- TextField (상품 명)
- TextField (상품 가격)
- TextField (상품 설명)
- ImageView (상품 사진 보여주기)
- Button(사진 찾아보기)