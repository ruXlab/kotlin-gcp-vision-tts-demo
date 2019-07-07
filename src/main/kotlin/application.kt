package vc.rux.demo.rememe

import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer


fun main() {
    routes(
        "/" bind GET to { req: Request -> Response(Status.OK).body(frontPage()) },
        "/second" bind GET to { req: Request -> Response(Status.OK).body(anotherPage()) },
        "/" bind POST to { req: Request ->
            val file = MultipartFormBody.from(req).file("file")
                ?: throw Exception("No file provided")

            upload(file.filename, file.content)

            Response(Status.MOVED_PERMANENTLY).header("Location", req.uri.path)

        },
        "/image/{id}" bind GET to { req: Request ->
            val item = storage.get(bucketName, req.path("id").orEmpty())
            val annotations = annotate(item.mediaLink)

            Response(Status.OK).body(picturePage(item.blobId.name, item.mediaLink, annotations))
        },
        "/speak/{id}" bind GET to { req: Request ->
            val item = storage.get(bucketName, req.path("id").orEmpty())
            val annotations = annotate(item.mediaLink)
            val text = "I found here " +
                    annotations.take(5).joinToString(", ") { it.label } +
                    annotations.getOrNull(6)?.let {" and ${it.label}" }

            Response(Status.OK).body(speak(text))
        }

    )
        .let(ServerFilters.CatchAll()::then)
        .asServer(SunHttp(9999))
        .start()
    println("App started, head to http://localhost:9999")
}