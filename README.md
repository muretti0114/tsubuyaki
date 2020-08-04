# Tsubuyaki: つぶやきWebアプリ

2020-08-04 中村匡秀

大規模ソフトウェア論・第6週「ソフトウェア実装」のSpring Boot ウォークスルーの例題．

## 概要
Webサイトに任意の人が「つぶやき」を残せる簡単な掲示板アプリ．

## ユースケース

### UC1：つぶやきを見る
- ユーザがメイン画面にアクセスすると，システムはこれまでに投稿されたつぶやきを一覧して表示する
- システムは各つぶやきを[ コメント（つぶやき本文） -- 投稿者 on 投稿日時] の１行で表示する

### UC2：つぶやきを投稿する
- ユーザは，メイン画面の投稿フォームに名前とつぶやきを入力し，投稿ボタンを押す．名前は省略可能．
- システムは，名前を確認し，省略されていた場合「名無しさん」にする．
- システムは，つぶやきに投稿日時を付与する．
- システムは，つぶやき（コメント，名前，投稿日時）をDBに保存する．
- システムは，UC1のメイン画面を表示する

### UC3: つぶやきを検索する
- ユーザは，メイン画面の検索フォームに検索キーワードを入力し，検索ボタンを押す．
- システムは，DBを検索し，キーワードをコメントに含むすべてのつぶやきを取得する．
- システムは，検索結果画面に検索されたつぶやきを一覧にして表示する．
- ユーザは，検索結果画面のつぶやきを見る．確認が終わったら「戻る」リンクでメイン画面に戻る．

## Spring Boot 諸元
- Project: Gradle Project
- Language: Java
- Spring Boot: 2.3.2
- Project Metadata:
    - Group: jp.kobe_u.cs.daikibo
    - Artifact: tsubuyaki
    - Name: tsubuyaki
- Packaging: War
- Java: 8
    - Dependencies:
    - Spring Data JPA
    - Spring Boot DevTools
    - Lombok
    - Spring Web
    - Thymeleaf
    - Validation
    - MySQL Driver

## @Entity Tsubuyaki

つぶやきWebアプリで管理するエンティティ．属性4つ
```Java
@Data
@Entity
public class Tsubuyaki {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //つぶやきエンティティの識別子
    String name;    //名前
    String comment; //コメント
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt; //作成日時
```

## @Repository TsubuyakiRepository

「つぶやき」エンティティのレポジトリ
- CRUD＋キーワード検索するカスタム・クエリを持つ
- findByCommentLike()は呼び出すときに， __%キーワード%__  のように文字列を%(SQLのワイルドカード)で挟むこと
```Java
@Repository
public interface TsubuyakiRepository extends CrudRepository<Tsubuyaki, Long>{
    public Iterable<Tsubuyaki> findByCommentLike(String keyword);
}
```

## @Service TsubuyakiService

つぶやきWebアプリのサービス．3つのドメイン処理を提供

```Java
@Service
public class TsubuyakiService {
    @Autowired
    TsubuyakiRepository repo; // レポジトリ
    // つぶやきを投稿
    public Tsubuyaki postTsubuyaki(Tsubuyaki t) {
    }
    // 全つぶやきを取得
    public List<Tsubuyaki> getAllTsubuyaki() {
    }
    // つぶやきをキーワードで検索
    public List<Tsubuyaki> searchTsubuyaki(String keyword) {
    }
}
```

## @Controller TsubuyakiController

つぶやきWebアプリのコントローラ．

### リクエストマッピング

|URI|HTTPメソッド|起動するJavaメソッド|表示するページ|
|:--|:--|:--|:--|
|/|GET|showIndex()|タイトル画面 (index.html)|
|/read|GET|showTsubuyakiList()|メイン画面 (tsubuyaki_list.html)|
|/read|POST|postTsubuyaki()|/readにリダイレクト|
|/search|GET|searchTsubuyaki()|検索結果画面（search_result.html）|

## HTMLテンプレート

つぶやきWebアプリのバウンダリクラスを実現するThymeleafテンプレート

|HTML|モデル属性・メソッド|説明|
|:---|:--------|:---|
|index.html||タイトル画面|
||はじめる()|GET /read|
|tsubuyaki_list.html||メイン画面|
||tsubuyakiList|全つぶやきのリスト|
||tsubuyakiForm|検索フォーム|
||投稿する()|POST /read tsubuyakiForm|
||検索する()|GET /search keyword|
|search_result.html||検索結果画面|
||tsubuyakiList|検索されたつぶやきのリスト|
||戻る()|GET /read|


