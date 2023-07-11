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
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    val rawData = """
         {
            "apps": [
                {
                    "image": "i1",
                    "title": "App1",
                    "description": "Descripción 1",
                    "developer": "Desarrollador 1",
                    "rating": 5,
                    "price": 0,
                    "category": 1,
                    "installed": true,
                    "screen": "s1",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        }
                    ]
                },
                {
                    "image": "i2",
                    "title": "App2",
                    "description": "Descripción 2",
                    "developer": "Desarrollador 2",
                    "rating": 2.5,
                    "price": 0,
                    "category": 2,
                    "installed": false,
                    "screen": "s2",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        },
                        {
                            "user": "User 2",
                            "comment": "Commentario 2"
                        }
                    ]
                },
                {
                    "image": "i3",
                    "title": "App3",
                    "description": "Descripción 3",
                    "developer": "Desarrollador 3",
                    "rating": 3,
                    "price": 0,
                    "category": 3,
                    "installed": true,
                    "screen": "s3",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        },
                        {
                            "user": "User 2",
                            "comment": "Commentario 2"
                        },
                        {
                            "user": "User 3",
                            "comment": "Commentario 3"
                        }
                    ]
                },
                {
                    "image": "i4",
                    "title": "App4",
                    "description": "Descripción 4",
                    "developer": "Desarrollador 4",
                    "rating": 1,
                    "price": 2.5,
                    "category": 1,
                    "installed": false,
                    "screen": "s1",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        }
                    ]
                },
                {
                    "image": "i5",
                    "title": "App5",
                    "description": "Descripción 5",
                    "developer": "Desarrollador 5",
                    "rating": 4.5,
                    "price": 3.4,
                    "category": 2,
                    "installed": true,
                    "screen": "s2",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        },
                        {
                            "user": "User 2",
                            "comment": "Commentario 2"
                        }
                    ]
                },
                {
                    "image": "i6",
                    "title": "App6",
                    "description": "Descripción 6",
                    "developer": "Desarrollador 6",
                    "rating": 2.5,
                    "price": 1.3,
                    "category": 3,
                    "installed": false,
                    "screen": "s3",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        },
                        {
                            "user": "User 2",
                            "comment": "Commentario 2"
                        },
                        {
                            "user": "User 3",
                            "comment": "Commentario 3"
                        }
                    ]
                },
                {
                    "image": "i7",
                    "title": "App7",
                    "description": "Descripción 7",
                    "developer": "Desarrollador 7",
                    "rating": 3,
                    "price": 0.3,
                    "category": 1,
                    "installed": false,
                    "screen": "s1",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        }
                    ]
                },
                {
                    "image": "i8",
                    "title": "App8",
                    "description": "Descripción 8",
                    "developer": "Desarrollador 8",
                    "rating": 0.5,
                    "price": 5,
                    "category": 2,
                    "installed": true,
                    "screen": "s2",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        },
                        {
                            "user": "User 2",
                            "comment": "Commentario 2"
                        }
                    ]
                },
                {
                    "image": "i9",
                    "title": "App9",
                    "description": "Descripción 9",
                    "developer": "Desarrollador 9",
                    "rating": 5,
                    "price": 0,
                    "category": 3,
                    "installed": false,
                    "screen": "s3",
                    "comments": [
                        {
                            "user": "User 1",
                            "comment": "Commentario 1"
                        },
                        {
                            "user": "User 2",
                            "comment": "Commentario 2"
                        },
                        {
                            "user": "User 3",
                            "comment": "Commentario 3"
                        }
                    ]
                }
            ]
        }
    """.trimIndent()
    val categories = arrayOf("Categoría", "Acción", "Aventura", "Estrategia")

    lateinit var appsList:Array<Game>
    lateinit var spinner:Spinner
    lateinit var tlApps:TableLayout
    lateinit var filteredApps:Array<Game>
    lateinit var appsMatrix:Array<Array<Game>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appsList = arrayOf()
        val jsonObject = JSONObject(rawData)
        val appsJSONArray = jsonObject.optJSONArray("apps")
        for (i in 0 until appsJSONArray.length()) {
            val appJSONObject = appsJSONArray.getJSONObject(i)
            val image = appJSONObject.optString("image")
            val title = appJSONObject.optString("title")
            val description = appJSONObject.optString("description")
            val developer = appJSONObject.optString("developer")
            val screen = appJSONObject.optString("screen")
            val rating = appJSONObject.optDouble("rating").toFloat()
            val price = appJSONObject.optDouble("price").toFloat()
            val categoryId = appJSONObject.optInt("category")
            val installed = appJSONObject.optBoolean("installed")

            var comments:Array<Comment> = arrayOf()
            val commentsJSONArray = appJSONObject.optJSONArray("comments")
            for (j in 0 until commentsJSONArray.length()) {
                val commentJSONObject = commentsJSONArray.getJSONObject(j)
                val user = commentJSONObject.optString("user")
                val commentString = commentJSONObject.optString("comment")

                val comment = Comment(user, commentString)
                comments += comment
            }

            val game = Game(image, title, description, developer, rating, price, categoryId, installed, screen, comments)
            appsList += game
        }

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