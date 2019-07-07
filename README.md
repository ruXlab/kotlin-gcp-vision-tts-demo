
Image uploader, annotator and voice describer 
=================


## What is it?

It's a demo web application written using kotlin language for JVM.
Essentially it's a website allowing user to upload pictures to GCP Storage,
automatically annotate content using GCP Vision and even read it using TTS GCP Text-to-Speech

It highlights use of kotlin for backend, Google Cloud Platform APIs and kotlin type-safe html builder

## What technologies does it use?

* [kotlin](http://localhost:9999) 1.3.41
* [kotlinx.html](https://github.com/Kotlin/kotlinx.html) 0.6.12
* [http4k](https://github.com/http4k/http4k) 3.163.0
* [Google Cloud Client SDK](https://github.com/googleapis/google-cloud-java) 1.36.0 



## How to start?

* Make sure you have java 8 installed
* Specify environment variables:
  - **GOOGLE_STORE_BUCKET** - name of the bucket for pictures
  - **GOOGLE_APPLICATION_CREDENTIALS** - path to the json credentials file, as [described in documentation](https://cloud.google.com/docs/authentication/getting-started)    
* run `./gradlew run` in project's root
* open your browser at [http://localhost:9999](http://localhost:9999)


## About project

It's a demo project originally developed for demo for [GDG Cloud London meetup](https://www.meetup.com/gdgcloud/events/261892954/)
by Ruslan Zaharov[ruX].

Blog: [https://rux.vc](https://rux.vc)

GitHub: [ruXlab/kotlin-gcp-vision-tts-demo](https://github.com/ruXlab/kotlin-gcp-vision-tts-demo)

