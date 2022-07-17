package com.gmail.jskim7018.booksearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.gmail.jskim7018.booksearchapp.Profile
import com.gmail.jskim7018.booksearchapp.model.book.BookModelFactory
import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import com.gmail.jskim7018.booksearchapp.view.bookdetail.BookDetailContract
import com.gmail.jskim7018.booksearchapp.view.booksearch.BookSearchActivity
import com.google.android.material.navigation.NavigationView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {
    var presenter: MainContract.Presenter? = null

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolBar: Toolbar
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "IT Book Search App"

//        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frameLayout)
        drawerLayout = findViewById(R.id.drawerLayout)
//        toolBar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationview)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener {
            //checking and unchecking of menu items
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isChecked = true
            it.isCheckable = true//What does this do? To check selected item
            previousMenuItem = it

            when (it.itemId) {
                //To invoke the fragment of the dashboard
                R.id.dashboard -> {
//                    openDashboard()
                    Toast.makeText(this@MainActivity, "Clicked Dashboard", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.profile -> {
                    supportFragmentManager
                        .beginTransaction().replace(
                            R.id.frameLayout,
                            Profile()
                        )
                        .addToBackStack("Profile").commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Profile"
                    Toast.makeText(this@MainActivity, "Clicked Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.aboutapp -> {
                    supportFragmentManager
                        .beginTransaction().replace(
                            R.id.frameLayout,
                            Aboutapp()
                        )
                        .addToBackStack("AboutApp").commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "About App"
                    Toast.makeText(this@MainActivity, "Clicked About App", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.favourites -> {

                    supportFragmentManager
                        .beginTransaction().replace(
                            R.id.frameLayout,
                            Favourites()
                        )
                        .addToBackStack("Favourites").commit()
                    drawerLayout.closeDrawers()

                    supportActionBar?.title = "Favourites"
                    Toast.makeText(this@MainActivity, "Clicked Favourites", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            return@setNavigationItemSelectedListener true
        }

        presenter = MainPresenter(this)

        search_button.setOnClickListener {
            val intent = Intent(this, BookSearchActivity::class.java)
            startActivity(intent)
        }

        presenter?.loadNewBookList()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun updateNewBookListView(bookList: MutableList<BookListItem>) {
        val newBooksAdapter = NewBooksAdapter(dataSet= bookList,context = this)
        new_books_recycler_view.adapter = newBooksAdapter
    }

    override fun showProgressUi() {
        new_books_recycler_view.visibility = RecyclerView.INVISIBLE
        loading_progress_ui.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressUi() {
        new_books_recycler_view.visibility = RecyclerView.VISIBLE
        loading_progress_ui.visibility = ProgressBar.INVISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter?.dispose()
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}