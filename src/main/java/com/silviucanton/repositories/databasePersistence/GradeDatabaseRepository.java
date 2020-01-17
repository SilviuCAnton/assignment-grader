package com.silviucanton.repositories.databasePersistence;

import com.silviucanton.domain.auxiliary.AssignmentGradeDTO;
import com.silviucanton.domain.auxiliary.StudentFinalGradeDto;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.repositories.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for grade storage - database data persistence
 */
@Repository
public interface GradeDatabaseRepository extends CrudRepository<Grade, GradeId> {

    @Query(value = "select s.firstname || ' ' || s.lastname as studentName,sum(coalesce(g.value,1)*(a.deadlineweek-a.startweek))/sum(a.deadlineweek-a.startweek) as finalGrade " +
            "from students s " +
            "cross join assignments a " +
            "left outer join grades g " +
            "on s.studentid = g.studentid and a.assignmentid = g.assignmentid " +
            "group by studentName", nativeQuery = true)
    List<StudentFinalGradeDto> getFinalGrades();

    @Query(value = "select s.firstname || ' ' || s.lastname as studentName,sum(coalesce(g.value,1)*(a.deadlineweek-a.startweek))/sum(a.deadlineweek-a.startweek) as finalGrade " +
            "from students s " +
            "cross join assignments a " +
            "left outer join grades g " +
            "on s.studentid = g.studentid and a.assignmentid = g.assignmentid " +
            "group by studentName having sum(coalesce(g.value,1)*(a.deadlineweek-a.startweek))/sum(a.deadlineweek-a.startweek) >= 5", nativeQuery = true)
    List<StudentFinalGradeDto> getPassedStudents();

    @Query(value = "select " +
            "s.firstname || ' ' || s.lastname as studentName,sum(coalesce(g.value,1)*(a.deadlineweek-a.startweek))/sum(a.deadlineweek-a.startweek) as finalGrade " +
            "from students s cross join assignments a " +
            "left outer join grades g on s.studentid = g.studentid and a.assignmentid = g.assignmentid " +
            "inner join (select s.studentid, count(a.assignmentid) nAssign from public.students s " +
            "inner join public.grades g on g.studentid = s.studentid " +
            "inner join public.assignments a on a.assignmentid = g.assignmentid " +
            "where g.weeknumber <= a.deadlineweek " +
            "group by s.studentid) onTime on s.studentid = onTime.studentid " +
            "group by studentName, onTime.nAssign " +
            "having onTime.nAssign = count(distinct a.assignmentid)", nativeQuery = true)
    List<StudentFinalGradeDto> getNeverLateStudents();

    @Query(value = "select a.description as assignmentName, avg(coalesce(g.value,1)) as grade from public.students s " +
            "cross join public.assignments a " +
            "left outer join public.grades g on g.studentid=s.studentid and g.assignmentid = a.assignmentid " +
            "group by a.description " +
            "order by grade " +
            "fetch first 1 rows only", nativeQuery = true)
    AssignmentGradeDTO getHardestAssignment();
}
