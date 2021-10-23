# Requirements

## 1 Basic Task Management

### 1.1 Task Hierarchy

To mimic UNIX filesystem:

```text
root
    - board1
        - list1
            - task1
                - subtask1-1
                - subtask1-2
            - task2
            - task3
        - list2
    - board2
        - list3
```

### 1.2 Task Properties

Implement the basic ones first, then incrementally augment.

* Title (A descriptive text)
* Details (Text with more details)
* Deadline (Date)
* Attachments (Multimedia type, e.g. picture)
* Parent task
* Children tasks

### 1.3 Task Operations