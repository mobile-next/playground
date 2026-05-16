SCHEME    = playground
PROJECT   = playground.xcodeproj
SDK       = iphonesimulator
BUILD_DIR = build
APP_NAME  = Playground
ZIP_NAME  = Playground.zip

.PHONY: all clean

all: $(ZIP_NAME)

$(ZIP_NAME):
	xcodebuild \
		-project $(PROJECT) \
		-scheme $(SCHEME) \
		-sdk $(SDK) \
		-configuration Debug \
		-derivedDataPath $(BUILD_DIR) \
		build
	cd $(BUILD_DIR)/Build/Products/Debug-iphonesimulator && zip -r $(CURDIR)/$(ZIP_NAME) $(SCHEME).app

clean:
	rm -rf $(BUILD_DIR) $(ZIP_NAME)
