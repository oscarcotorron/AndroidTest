package com.osche.androidtest

import android.app.ActionBar.LayoutParams
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    val categories = arrayOf("Categoría", "Acción", "Aventura", "Estrategia")
    val appsList =arrayOf(
        Game("i1", "App 1", "Descripción 1","Desarrollador 1", 5.0F, 0F,1, true,"s1", arrayOf(
            Comment("User 1", "Coment 1"),
        )),
        Game("i2", "App 2", "Descripción 2","Desarrollador 2", 2.0F, 0F,2, false,"s2", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
        Game("i3", "App 3", "Descripción 3","Desarrollador 3", 3.5F,0F, 3, false,"s3", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
            Comment("User 3", "Coment 3"),
        )),
        Game("i4", "App 4", "Descripción 4","Desarrollador 4", 1.0F, 1.6F,1, false,"s1", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
        Game("i5", "App 5", "Descripción 5","Desarrollador 5", 3.0F, 2.5F,2, false,"s2", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
        Game("i6", "App 6", "Descripción 6","Desarrollador 6", 2.0F, 1.6F,3, false,"s1", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
        Game("i7", "App 7", "Descripción 7","Desarrollador 7", 4.5F, 2.5F,1, false,"s2", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
        Game("i8", "App 8", "Descripción 8","Desarrollador 8", 3.0F, 1.6F,2, false,"s1", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
        Game("i9", "App 9", "Descripción 9","Desarrollador 9", 2.5F, 2.5F,3, false,"s2", arrayOf(
            Comment("User 1", "Coment 1"),
            Comment("User 2", "Coment 2"),
        )),
    )

    lateinit var spinner:Spinner
    lateinit var tlApps:TableLayout
    lateinit var filteredApps:Array<Game>
    lateinit var appsMatrix:Array<Array<Game>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        listeners()
        filteredApps = appsList.copyOf()
        appsMatrix = buildAppMatrix(filteredApps,3)

        renderAppMatrix()
    }

    fun renderAppMatrix() {
        tlApps.removeAllViews()
        for (i in appsMatrix.indices) {
            var row = TableRow(this)
            for (j in appsMatrix[i].indices) {
                val card = LayoutInflater.from(this).inflate(R.layout.card_app, null,false)
                card.setOnClickListener {
                    openDetails(appsMatrix[i][j])
                }
                val ivLogo = card.findViewById<ImageView>(R.id.ivLogo)
                val tvName = card.findViewById<TextView>(R.id.tvName)
                val tvDeveloper = card.findViewById<TextView>(R.id.tvDeveloper)
                val tvPrice = card.findViewById<TextView>(R.id.tvPrice)
                val rbRatingG = card.findViewById<RatingBar>(R.id.rbRatingG)
                val rbRatingR = card.findViewById<RatingBar>(R.id.rbRatingR)

                val price = appsMatrix[i][j].price

                ivLogo.setImageResource(resources.getIdentifier(appsMatrix[i][j].image,"mipmap", packageName))
                tvName.text = appsMatrix[i][j].title
                tvDeveloper.text = appsMatrix[i][j].developer
                tvPrice.text = if (price < 0.5) "Free" else "$${price}"

                if (appsMatrix[i][j].rating >= 3) {
                    rbRatingG.visibility = View.VISIBLE
                    rbRatingR.visibility = View.INVISIBLE
                    rbRatingG.rating = appsMatrix[i][j].rating
                } else {
                    rbRatingG.visibility = View.INVISIBLE
                    rbRatingR.visibility = View.VISIBLE
                    rbRatingR.rating = appsMatrix[i][j].rating
                }

                row.addView(card)
            }
            tlApps.addView(row)
        }
    }

    fun findViews() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner = findViewById<Spinner>(R.id.spCategories)
        spinner.adapter = adapter

        tlApps = findViewById<TableLayout>(R.id.tlApps)
    }

    fun listeners() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, category: Int, p3: Long) {
                filteredApps = arrayOf()
                for (game in appsList) {
                    if (category == 0) {
                        filteredApps += game
                    } else {
                        if (game.categoryId == category) {
                            filteredApps += game
                        }
                    }
                }
                appsMatrix = buildAppMatrix(filteredApps,3)
                renderAppMatrix()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    fun buildAppMatrix(gameArray:Array<Game>, columns:Int):Array<Array<Game>> {
        var gameMatrix: Array<Array<Game>> = arrayOf(arrayOf())
        var position = 0
        var row: Array<Game> = arrayOf()
        for (game in gameArray) {
            row += game
            if (position < columns - 1) {
                position++
            } else {
                gameMatrix += row
                row = arrayOf()
                position = 0
            }
        }
        gameMatrix += row
        return gameMatrix
    }

    fun openDetails(game: Game) {
        val builder= AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.m_details,null)

        val ivScreen = view.findViewById<ImageView>(R.id.ivScreen)
        val ivLogo = view.findViewById<ImageView>(R.id.ivLogo)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvDeveloper = view.findViewById<TextView>(R.id.tvDeveloper)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val llComments = view.findViewById<LinearLayout>(R.id.llComments)
        val btnInstall = view.findViewById<Button>(R.id.btnInstall)
        val btnClose = view.findViewById<Button>(R.id.btnClose)
        val rbRatingG = view.findViewById<RatingBar>(R.id.rbRatingG)
        val rbRatingB = view.findViewById<RatingBar>(R.id.rbRatingR)

        btnInstall.visibility = if (game.installed) View.GONE else View.VISIBLE

        if (game.rating >= 3) {
            rbRatingG.visibility = View.VISIBLE
            rbRatingB.visibility = View.INVISIBLE
            rbRatingG.rating = game.rating
        } else {
            rbRatingG.visibility = View.INVISIBLE
            rbRatingB.visibility = View.VISIBLE
            rbRatingB.rating = game.rating
        }


        ivScreen.setImageResource(resources.getIdentifier(game.screen,"mipmap", packageName))
        ivLogo.setImageResource(resources.getIdentifier(game.image,"mipmap", packageName))
        tvName.text = game.title
        tvDeveloper.text = game.developer
        tvDescription.text = game.description
        tvPrice.text = if (game.price < 0.5) "Free" else "$${game.price}"

        for (comment in game.comments) {
            val row=layoutInflater.inflate(R.layout.row_comment,null)

            val tvName = row.findViewById<TextView>(R.id.tvName)
            val tvComment = row.findViewById<TextView>(R.id.tvComment)

            tvName.text = comment.user
            tvComment.text = comment.comment

            llComments.addView(row)
        }

        builder.setView(view)
        val popup=builder.create()

        btnClose.setOnClickListener {
            popup.dismiss()
        }

        popup.show()
    }
}