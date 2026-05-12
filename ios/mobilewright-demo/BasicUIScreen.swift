import SwiftUI
import PhotosUI

struct BasicUIScreen: View {
    @State private var textValue = ""
    @State private var passwordValue = ""
    @State private var isToggled = false
    @State private var sliderValue = 0.5
    @State private var stepperValue = 0
    @State private var selectedPicker = 0
    @State private var selectedDate = Date()
    @State private var multilineText = ""
    @State private var selectedSegment = 0
    @State private var showAlert = false
    @State private var showConfirmation = false
    @State private var selectedPhoto: PhotosPickerItem? = nil
    @State private var menuLastAction = "None"

    var body: some View {
        Form {
            Section("Text Input") {
                TextField("Enter text", text: $textValue)
                    .accessibilityIdentifier("text_field")
                SecureField("Enter password", text: $passwordValue)
                    .accessibilityIdentifier("password_field")
                TextEditor(text: $multilineText)
                    .frame(height: 80)
                    .accessibilityIdentifier("multiline_text")
            }

            Section("Controls") {
                Toggle("Toggle switch", isOn: $isToggled)
                    .accessibilityIdentifier("toggle")
                Stepper("Count: \(stepperValue)", value: $stepperValue, in: 0...100)
                    .accessibilityIdentifier("stepper")
                Button("Reset Counter") { stepperValue = 0 }
                    .accessibilityIdentifier("button")
            }

            Section("Slider") {
                Slider(value: $sliderValue, in: 0...1)
                    .accessibilityIdentifier("slider")
                Text(String(format: "Value: %.2f", sliderValue))
                    .foregroundColor(.secondary)
            }

            Section("Picker") {
                Picker("Select option", selection: $selectedPicker) {
                    Text("Option A").tag(0)
                    Text("Option B").tag(1)
                    Text("Option C").tag(2)
                }
                .accessibilityIdentifier("picker")

                Picker("Segment", selection: $selectedSegment) {
                    Text("One").tag(0)
                    Text("Two").tag(1)
                    Text("Three").tag(2)
                }
                .pickerStyle(.segmented)
                .accessibilityIdentifier("segmented_control")
            }

            Section("Date") {
                DatePicker("Pick a date", selection: $selectedDate, displayedComponents: .date)
                    .accessibilityIdentifier("date_picker")
            }

            Section("Progress") {
                ProgressView("Loading…", value: sliderValue)
                    .accessibilityIdentifier("progress_view")
                ProgressView()
                    .accessibilityIdentifier("activity_indicator")
            }

            Section("Labels") {
                Label("Info label", systemImage: "info.circle")
                    .accessibilityIdentifier("label")
                Text("Plain text label")
                    .accessibilityIdentifier("plain_label")
                Link("Tap to open link", destination: URL(string: "https://mobilewright.dev")!)
                    .accessibilityIdentifier("link")
            }

            Section("Dialogs & Menus") {
                Button("Show Alert") {
                    showAlert = true
                }
                .accessibilityIdentifier("alert_button")
                .alert("Alert Title", isPresented: $showAlert) {
                    Button("OK", role: .cancel) {}
                    Button("Destructive", role: .destructive) {}
                } message: {
                    Text("This is an alert message.")
                }

                Button("Show Confirmation") {
                    showConfirmation = true
                }
                .accessibilityIdentifier("confirmation_button")
                .confirmationDialog("Are you sure?", isPresented: $showConfirmation, titleVisibility: .visible) {
                    Button("Confirm", role: .destructive) {}
                    Button("Cancel", role: .cancel) {}
                } message: {
                    Text("This action cannot be undone.")
                }

                Menu("Menu (last: \(menuLastAction))") {
                    Button("Action One") { menuLastAction = "One" }
                    Button("Action Two") { menuLastAction = "Two" }
                    Divider()
                    Button("Destructive", role: .destructive) { menuLastAction = "Destructive" }
                }
                .accessibilityIdentifier("menu")
            }

            Section("Share & Photos") {
                ShareLink(item: URL(string: "https://mobilewright.dev")!) {
                    Label("Share link", systemImage: "square.and.arrow.up")
                }
                .accessibilityIdentifier("share_link")

                PhotosPicker(selection: $selectedPhoto, matching: .images) {
                    Label(
                        selectedPhoto == nil ? "Pick a photo" : "Photo selected",
                        systemImage: "photo"
                    )
                }
                .accessibilityIdentifier("photos_picker")
            }
        }
        .navigationTitle("Basic UI")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NavigationStack {
        BasicUIScreen()
    }
}
