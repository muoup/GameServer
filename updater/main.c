#include <stdlib.h>
#include <sys/stat.h>
#include <stdio.h>
#include <unistd.h>

int checkVersion() {
    const char* folder = "src";
    struct stat sb;

    if (stat(folder, &sb) == 0 && S_ISDIR(sb.st_mode)) {
        return 1;
    } else {
        return 0;
    }
}

int main() {
    if (checkVersion()) {
        if (system("git pull")) { //if command errors
            printf("The updater failed to pull from the remote repository. Please download the new version.");
            exit(1);
        } else {
            if (system("mvn package")) {
                printf("The updater failed to rebuild the package. Is maven installed?");
                exit(2);
            } else {
                printf("Update complete. The build can be found in the target/ dir as a .jar");
            }
        }
    } else {
        printf("The updater detects that you did not build the program from source.");
        printf("Acquiring new archive...");
        if (system("wget -r --no-parent -A '*-Server.jar' https://github.com/masterdust/gameserver/releases/latest/download/")) {
            printf("wget failed to acquire the latest release. You may have to download manually, or check your internet connection.");
            exit(1);
        } else {
            printf("Update complete.");
        }
    }
    return 0;
}
