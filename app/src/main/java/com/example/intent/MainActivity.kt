package com.example.intent

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image = findViewById<ImageView>(R.id.imageView)
        val goToWebButton = findViewById<Button>(R.id.webButton)
        val url = "https://nickelodeon.fandom.com/wiki/SpongeBob_SquarePants_(character)"
        val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        // Créez un intent pour ouvrir la galerie
        val intentImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                // Mettez à jour l'ImageView avec l'image sélectionnée
                image.setImageURI(uri)
            }
        }
//*************************************** IMAGES dans le tel ***************************************
        val imageBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.door)

        // Définissez le nom de fichier et le chemin d'accès où vous souhaitez sauvegarder l'image
        val fileName = "door.png"
        val filePath =
            Environment.getExternalStorageDirectory().toString() + "/Pictures/" + fileName

        // Créez un fichier de sortie
        val file = File(filePath)

        // Écrivez l'image dans le fichier
        FileOutputStream(file).use { out ->
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Actualisez la galerie pour qu'elle détecte la nouvelle image
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = Uri.fromFile(file)
        sendBroadcast(mediaScanIntent)
//*************************************************************************************************

        goToWebButton.setOnClickListener {
            startActivity(intentWeb)
        }
        image.setOnClickListener {
            // Lancez l'activité de sélection d'image et attendez le résultat
            getContent.launch("image/*") // Vous pouvez spécifier le type MIME souhaité ici
        }

    }


}