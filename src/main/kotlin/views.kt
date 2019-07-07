package vc.rux.demo.rememe

import kotlinx.html.*
import kotlinx.html.stream.createHTML


fun page(content: FlowContent.() -> Unit) = createHTML().html {
    head {
        styleLink("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
    }

    body {
        div(classes = "container") {
            content(this)
        }
    }
}

fun frontPage() = page {
    div(classes = "row") {
        div(classes = "col-md-12") {
            h1 { +"Gallery" }
        }
        bucket.list().iterateAll().forEach { blob ->
            div(classes = "col-md-4") {
                a(href = "/image/${blob.blobId.name}") {
                    img(classes = "img-thumbnail", src = blob.mediaLink)
                }
            }

        }
    }

    hr {}

    form(action = "/", encType = FormEncType.multipartFormData, method = FormMethod.post) {
        +"Upload file"
        input(type = InputType.file, name = "file") {  }
        button { +"Upload" }
    }
}

fun anotherPage() = page {
    h1 { +"another page" }
}

fun picturePage(imageId: String, imageUrl: String, annotations: List<ImageAnnotation>) = page {
    div(classes = "row") {
        div(classes = "col-md-12") {
            a(href = "/") { +"<< Home" }
            h1 { +"Picture $imageId" }
        }

        br

        div(classes = "col-md-3") {
            annotations.forEach { annotation ->
                div {
                    b { +annotation.label }
                    div(classes = "progress") {
                        div(classes = "progress-bar") {
                            style = "width: ${annotation.score}%"
                            +"${annotation.score}%"
                        }
                    }
                }
            }

            br
            audio {
                style = "width: 100%"
                attributes["controls"] = "true"
                source {
                    attributes["src"] = "/speak/$imageId"
                    attributes["type"] = "audio/mpeg"
                }
                +"Audio is not supported :("
            }

        }

        div(classes = "col-md-9") {
            img(classes = "img-fluid", src = imageUrl)
        }
    }

}
