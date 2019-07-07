package vc.rux.demo.rememe

import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.google.cloud.texttospeech.v1.*
import com.google.cloud.vision.v1.*
import java.io.InputStream
import java.util.*

val bucketName = System.getenv("GOOGLE_STORE_BUCKET")
    ?: throw Exception("You need to specify GOOGLE_STORE_BUCKET environment variable")

val googleClientCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
    ?: throw Exception("You need to specify GOOGLE_APPLICATION_CREDENTIALS environment variable")

val storage = StorageOptions.getDefaultInstance().service
val bucket = storage.get(bucketName)
    ?: throw Exception("Bucket $bucketName does not exist.")
val vision = ImageAnnotatorClient.create()
val tts = TextToSpeechClient.create()


/**
 * Performs TTS synthesis
 * @return mp3 bytearray's input stream
 */
fun speak(text: String): InputStream {
    val input = SynthesisInput.newBuilder()
        .setText(text)
        .build()

    // Build the voice request, select the language code ("en-US") and the ssml voice gender
    // ("neutral")
    val voice = VoiceSelectionParams.newBuilder()
        .setLanguageCode("en-UK")
        .setSsmlGender(SsmlVoiceGender.NEUTRAL)
        .build()

    // Select the type of audio file you want returned
    val audioConfig = AudioConfig.newBuilder()
        .setAudioEncoding(AudioEncoding.MP3)
        .build()

    // Perform the text-to-speech request on the text input with the selected voice parameters and
    // audio file type
    val response = tts.synthesizeSpeech(
        input, voice,
        audioConfig
    )
    return response.audioContent.toByteArray().inputStream()
}

/**
 * Uploads file
 * @return uploaded file information
 */
fun upload(basename: String, data: InputStream): Blob {
    return bucket.create(
        "${Date().time}-${basename}",
        data,
        Bucket.BlobWriteOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
    )
}

data class ImageAnnotation(val label: String, val score: Int)

/**
 * Annotates picture by given url using GCP Vision
 * @return list of labels and scores
 */
fun annotate(url: String): List<ImageAnnotation> {
    val img = Image
        .newBuilder()
        .setSource(ImageSource.newBuilder().setImageUri(url))
    val request = AnnotateImageRequest.newBuilder()
        .addFeatures(Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build())
        .setImage(img)
        .build()
    val annotations = vision.batchAnnotateImages(listOf(request))

    return annotations.getResponses(0).labelAnnotationsList.map {
        ImageAnnotation(it.description, (it.score*100).toInt())
    }
}

