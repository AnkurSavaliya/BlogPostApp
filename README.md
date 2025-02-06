
# Blog Post App

This is used to create the blog and comment the blog on the other user created blog.

This App containt the main three module
- User
- Blog
- Comment

First we need to create the User, then we can create the blog or comment.

Before the creating the comment we need the blog, to add the comment for the perticular blog.

When we delete the user, that time all the blog and comment are deleted that are created by this user.



**Note**
- As of now we are not implemented the Authorization, So any one can delete the any user.






## API Reference

#### Get User details

```http
  GET /user/{userId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `Number` | **Required**. Your user id |

#### Create User

```http
  POST /user/create-user
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `{"email":"test@test.com","userName":"test"}`      | `Request Body` | **Required** |


#### Update User

```http
  PUT /user/update-user/{userId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
|      `userId`     | `Number`| **Required** Id of the user to update the details                          |
| `{"email":"test@test.com","userName":"test"}}`      | `Request Body` | **Required** required data to udpate the details |

#### Delete User

```http
  DELETE /user/{userId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `Number` | **Required**. User id to delete|

#### Ge Blog details

```http
  GET /blog/{blogId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `blogId` | `Number` | **Required**. Blog id to fetch data|

#### Create Blog

```http
  POST /blog/create-blog
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `{"title":"My First Blog","blogText":"Blog text","creatorUserId": 1}`      | `Request Body` | **Required**  Required data to create blog|

#### Update Blog

```http
  PUT /blog/update-blog/{blogId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
|      `blogId`     | `Number`| **Required** Id of the blog to update the details                          |
| `{"title":"My First Blog","blogText":"Blog text","creatorUserId": 1}`       | `Request Body` | **Required** required data to udpate the details |

#### Delete Blog

```http
  DELETE /blog/{blogId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `blogId` | `Number` | **Required**. Blog id to delete|

#### Get Comment

```http
  GET /comments/{commentId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `commentId` | `Number` | **Required**. Comment id to fetch data|

#### Create Comment

```http
  POST /comments/create-comment
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `{"commentText":"it is a good blog","blogId":1,"creatorId":1}`      | `Request Body` | **Required**  Required data to create comment|

#### Delete Comment

```http
  DELETE /comments/{commentId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `commentId` | `Number` | **Required**. Comment id to delete|

## Run Locally

Start the server

```bash
  mvn spring-boot:run
```


## Running Tests

To run tests, run the following command

```bash
  mvn test
```

