# 2018-Competition-Code
CDM Robotics Team #6072
<br>http://www.tritontech6072.com

### Overview ###
All code for Triton Tech's 2017-2018 competition season will be stored in this repo. Any sort of additional unrelated code or historical projects should be placed in a different repo. 

### Getting Started ###
1. Create a directory on your machine in your home directory called triton-tech
```
mkdir triton-tech
```
2. Open your shell and cd into the newly created triton-tech directory
```
cd /Users/NAME/triton-tech/
```
3. Initialize git and add a remote named origin for this repo
```
git init
git remote add origin repo_url_here
```
4. Run the following command to pull the remote code:
```
git fetch origin master
```

### Contribution Guidlines ###
The latest stable code base will be kept on the master branch. Developmental code will be kept on a branch called dev. If you wish to contribute to the code base, take a ticket (or add a ticket if necessary) and checkout on a new branch off dev. Your branch should be named according to the ticket number and feature. For example, here is how we would checkout for a fake ticket with the #1010.
```
git checkout -b 1010_climber_fix
```
When you are done writing code on your branch, commit and push it. To do so, run the following commands:
```
git add -A
git commit -m "message here"
git push origin BRANCH_NAME_HERE
```
When you are all done with a ticket, mark it for code review needed and generate a pull request.
