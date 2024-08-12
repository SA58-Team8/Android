# FunSG Mobile Part (Android)
**NOTE:** this part of project is based on Android 10 (API29), using Java to bulid and run.

### Project Introduction:  

This project is to let user know about the events and groups with the same intrest based on their unique MBTI type. It uses machine learning to make suggestions so user can gain differents events on this app. User can join any group and event, even to discover past event amazing photos. We hope that this project can let people to find interest and make friends in every participation.

**How to run:**
### 1. Clone the Repository:  
After cloning code into local directory, please use Android Studio to build and run
### 2. Modify Configuration:  
This project need the backend server to acquire the data and the google cloud server api to build embeded google map.  
Please change the codes in  
"IPAddress.java"  
and  
"strings.xml".  

For example:  
in strings.xml: please change  
\<string name="google_map_key"\>YOUR_GOOGLE_MAPS_API_KEY\</string\>  
in IPAddress.java : please change public static final String ipAddress


## Acknowledgments

This whole FunSG project is created by NUS-ISS SA58 Team8 Members. Thank everyone again for making great contributions.  

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.



