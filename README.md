# TwitterSentimentAnalysis

Projenin amacı, Twitter kullanıcılarının paylaştığı tweetlerdeki anlamların, olumlu ya da olumsuz olarak sınıflandırılmasıdır. Proje sadece Türkçe tweetlerde sınıflandırma yapar.
Projenin bu kısmı Arayüz tasarımı, Doğal Dil İşleme ve tweetlerin çekilmesini kapsamaktadır.

The purpose of the project, the meanings of tweets that Twitter users share are classified as positive or negative.The project only classifies in Turkish tweets.
This part of the project includes Interface design, Natural Language Processing and downloading tweets.

#Gerekli Döküman ve Uygulamalar

#Required Documents and Applications
1. [Twitter Developer App](https://developer.twitter.com/)
2. [Twitter4J](http://twitter4j.org/en/index.html) 
3. [Zemberek](https://github.com/ahmetaa/zemberek-nlp)
4. [JSF Primefaces](https://www.primefaces.org/documentation/)
5. [ElasticSearch](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html)

#Projenin Çalışma Adımları

#Project development steps
1. Arama kutusuna aratılacak olan cümle,tweet veya kullanıcı adı girilir ve kaydet kısmındaki tick işaretlenir. :white_check_mark:

       enter the sentence, tweet or username to be searched and tick the tick in the save. :white_check_mark:
2. Kelime veya cümle önerisi geldiğinde isteğe göre yeni arama yapılabilir. :white_check_mark:

       Search again by word or sentence suggestion. :white_check_mark:
      
3. Kaydedilen veriler CSV dosyası formatında bilgisayara kaydedilir. :white_check_mark:

       The recorded data is saved to the computer in CSV file format.

4. Bu veriler Makine Öğrenmesi uygulaması için Python'a test verisi olarak gönderilir. :white_check_mark:

       These data are sent as test data to Python for Machine Learning application. :white_check_mark:
       
