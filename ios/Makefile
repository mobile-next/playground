SCHEME       = playground
PROJECT      = playground.xcodeproj
BUILD_DIR    = build
VERSION     ?= dev
ZIP_NAME     = Playground-$(VERSION).zip
IPA_NAME     = Playground-$(VERSION).ipa
ARCHIVE_PATH = $(BUILD_DIR)/$(SCHEME).xcarchive
IPA_DIR      = $(BUILD_DIR)/ipa

.PHONY: all clean

all: $(ZIP_NAME) $(IPA_NAME)

$(ZIP_NAME):
	xcodebuild \
		-project $(PROJECT) \
		-scheme $(SCHEME) \
		-sdk iphonesimulator \
		-configuration Debug \
		-derivedDataPath $(BUILD_DIR) \
		build
	cd $(BUILD_DIR)/Build/Products/Debug-iphonesimulator && zip -r $(CURDIR)/$(ZIP_NAME) $(SCHEME).app

$(IPA_NAME):
	xcodebuild archive \
		-project $(PROJECT) \
		-scheme $(SCHEME) \
		-configuration Debug \
		-destination "generic/platform=iOS" \
		-archivePath $(ARCHIVE_PATH) \
		CODE_SIGN_IDENTITY="" \
		CODE_SIGNING_REQUIRED=NO \
		CODE_SIGNING_ALLOWED=NO
	mkdir -p $(IPA_DIR)/Payload
	cp -r $(ARCHIVE_PATH)/Products/Applications/$(SCHEME).app $(IPA_DIR)/Payload/
	cd $(IPA_DIR) && zip -r $(CURDIR)/$(IPA_NAME) Payload
	rm -rf $(IPA_DIR)

clean:
	rm -rf $(BUILD_DIR) Playground-*.zip Playground-*.ipa
