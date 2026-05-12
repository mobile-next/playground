package com.mobilenext.playground

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class BasicUIActivity : AppCompatActivity() {

    private var stepperCount = 0

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_ui)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupStepper()
        setupSlider()
        setupPicker()
        setupDatePicker()
        setupAlertButton()
        setupConfirmationButton()
        setupMenu()
        setupShareButton()
        setupPhotosPicker()
        setupLink()
    }

    private fun setupStepper() {
        val stepperValue = findViewById<TextView>(R.id.stepper_value)

        findViewById<Button>(R.id.stepper_decrement).setOnClickListener {
            if (stepperCount > 0) stepperCount--
            stepperValue.text = stepperCount.toString()
        }

        findViewById<Button>(R.id.stepper_increment).setOnClickListener {
            if (stepperCount < 100) stepperCount++
            stepperValue.text = stepperCount.toString()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            stepperCount = 0
            stepperValue.text = "0"
        }
    }

    private fun setupSlider() {
        val sliderValue = findViewById<TextView>(R.id.slider_value)
        val progressView = findViewById<ProgressBar>(R.id.progress_view)

        findViewById<SeekBar>(R.id.slider).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sliderValue.text = String.format("%.2f", progress / 100.0)
                progressView.progress = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun setupPicker() {
        val spinner = findViewById<Spinner>(R.id.picker)
        val options = arrayOf("Option A", "Option B", "Option C")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
    }

    private fun setupDatePicker() {
        val dateValue = findViewById<TextView>(R.id.date_value)

        findViewById<Button>(R.id.date_picker).setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    dateValue.text = String.format("%04d-%02d-%02d", year, month + 1, day)
                },
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupAlertButton() {
        findViewById<Button>(R.id.alert_button).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("This is an alert message.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("Destructive") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun setupConfirmationButton() {
        findViewById<Button>(R.id.confirmation_button).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("This action cannot be undone.")
                .setPositiveButton("Confirm") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun setupMenu() {
        val menuLastAction = findViewById<TextView>(R.id.menu_last_action)

        findViewById<Button>(R.id.menu).setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add("Action One")
            popup.menu.add("Action Two")
            popup.menu.add("Destructive")
            popup.setOnMenuItemClickListener { item ->
                menuLastAction.text = "Last action: ${item.title}"
                true
            }
            popup.show()
        }
    }

    private fun setupShareButton() {
        findViewById<Button>(R.id.share_link).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://mobilewright.dev")
            }
            startActivity(Intent.createChooser(intent, "Share"))
        }
    }

    private fun setupPhotosPicker() {
        findViewById<Button>(R.id.photos_picker).setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun setupLink() {
        findViewById<TextView>(R.id.link).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mobilewright.dev")))
        }
    }
}
