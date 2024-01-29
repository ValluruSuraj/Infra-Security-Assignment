I've built an android application that stored employee details. It uses the SQLite and Requests.

## 1. XSS (Cross-Site Scripting):
### Vulnerable Code:
In my Android application, I had a WebView that rendered user-provided content without proper sanitation.
```kotlin
String userProvidedContent = "<script>alert('XSS Attack');</script>";
webView.loadData(userProvidedContent, "text/html", "UTF-8");
```

### Fix:

To mitigate XSS attacks, I've incorporated input sanitization using the OWASP Java HTML Sanitizer library.

```kotlin
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

String userProvidedContent = "<script>alert('XSS Attack');</script>";
PolicyFactory sanitizer = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
String sanitizedContent = sanitizer.sanitize(userProvidedContent);
webView.loadData(sanitizedContent, "text/html", "UTF-8");
```


## 2. SQL Injection:
### Vulnerable Code:
I had a vulnerability in my code where user input was directly concatenated into an SQL query, making it susceptible to SQL injection.
```kotlin
String userInput = "'; DROP TABLE employees; --";
String sqlQuery = "SELECT * FROM employees WHERE name = '" + userInput + "'";
```

### Fix:

To address this issue, I've switched to using parameterized queries or prepared statements to prevent SQL injection.

```kotlin
String userInput = "'; DROP TABLE employees; --";
String sqlQuery = "SELECT * FROM employees WHERE name = ?";
SQLiteDatabase db = getReadableDatabase();
Cursor cursor = db.rawQuery(sqlQuery, new String[]{userInput});
```

3. CSRF (Cross-Site Request Forgery):
### Vulnerable Code:

In my Android app, I was sending requests to the server without implementing anti-CSRF protection.
```kotlin
// Server-side endpoint
@POST
@Path("/updateEmployee")
public Response updateEmployee(@FormParam("employeeId") long employeeId, @FormParam("name") String name) {
    Employee updatedEmployee = fetchEmployeeById(employeeId);
    if (updatedEmployee != null) {
        updatedEmployee.setName(name);
        saveUpdatedEmployee(updatedEmployee);
        return Response.ok("Employee updated successfully").build();
    } else {
        return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
    }
}
```

### Fix:

Implement anti-CSRF tokens on the server side and include them in the requests from your Android app. Validate these tokens on the server side for each request.


Here's a simplified example of how you might add CSRF protection to a server endpoint:

```kotlin
@POST
@Path("/updateEmployee")
public Response updateEmployee(
    @FormParam("employeeId") long employeeId,
    @FormParam("name") String name,
    @HeaderParam("X-CSRF-Token") String csrfToken
) {
    // Validate CSRF token
    if (isValidCSRFToken(csrfToken)) {
        Employee updatedEmployee = fetchEmployeeById(employeeId);
        if (updatedEmployee != null) {
            updatedEmployee.setName(name);
            saveUpdatedEmployee(updatedEmployee);
            return Response.ok("Employee updated successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }
    } else {
        return Response.status(Response.Status.FORBIDDEN).entity("CSRF token is invalid").build();
    }
}
```
