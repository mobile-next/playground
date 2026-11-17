package com.mobilenext.playground

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.*
import kotlin.math.hypot
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
        setupDragAndDrop()
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

    private fun setupDragAndDrop() {
        val dragBox = findViewById<View>(R.id.drag_box)
        val dropTarget = findViewById<View>(R.id.drop_target)
        val touchSlop = ViewConfiguration.get(this).scaledTouchSlop
        var downRawX = 0f
        var downRawY = 0f
        var startTranslationX = 0f
        var startTranslationY = 0f
        var isDragging = false

        dragBox.setOnClickListener {
            showDragAndDropDialog("Drag this box into the dotted box on the right.") { }
        }

        dragBox.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downRawX = event.rawX
                    downRawY = event.rawY
                    startTranslationX = view.translationX
                    startTranslationY = view.translationY
                    isDragging = false
                    // keep the ScrollView from stealing the gesture
                    view.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - downRawX
                    val deltaY = event.rawY - downRawY
                    if (!isDragging && hypot(deltaX, deltaY) > touchSlop) {
                        isDragging = true
                    }
                    if (isDragging) {
                        view.translationX = startTranslationX + deltaX
                        view.translationY = startTranslationY + deltaY
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        view.performClick()
                    } else if (isCenterInside(view, dropTarget)) {
                        showDragAndDropDialog("You score one point.") { returnToStart(view) }
                    } else {
                        // a box left outside its section can no longer receive touches, so a miss goes home
                        returnToStart(view)
                    }
                }
                MotionEvent.ACTION_CANCEL -> returnToStart(view)
            }
            true
        }
    }

    private fun returnToStart(box: View) {
        box.animate().translationX(0f).translationY(0f).start()
    }

    private fun isCenterInside(box: View, target: View): Boolean {
        val boxLocation = IntArray(2)
        val targetLocation = IntArray(2)
        box.getLocationOnScreen(boxLocation)
        target.getLocationOnScreen(targetLocation)
        val centerX = boxLocation[0] + box.width / 2
        val centerY = boxLocation[1] + box.height / 2
        return centerX in targetLocation[0]..(targetLocation[0] + target.width) &&
            centerY in targetLocation[1]..(targetLocation[1] + target.height)
    }

    private fun showDragAndDropDialog(message: String, onDismissed: () -> Unit) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onDismissed()
            }
            .show()
    }

    private fun setupLink() {
        findViewById<TextView>(R.id.link).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mobilewright.dev")))
        }
    }
}
