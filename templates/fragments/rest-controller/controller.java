package $pack

import $modelPath

import java.util.List

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid
import org.springframework.web.bind.annotation.*

/**
 * Attenzione: se ritieni di dover modificare questo controller
 * considera di creare un manager e portare lì tutta
 * la logica di dominio. Qui deve restare solo 
 * quanto concerne il recupero delle informazioni (modello,
 * id, ecc.) tramite HTTP.
 * 
 * @author nicola
 *
 */
@RestController
@RequestMapping("/api")
class $Controller {
	
	@RequestMapping(value="/$model", method=RequestMethod.POST)
	void create(@Valid @RequestBody $Model $model) {
		${model}.persist()
	}
	
	@RequestMapping(value="/$model", method=RequestMethod.PUT)
	void update(@Valid @RequestBody $Model $model)  {
		$Model attached$Model = ${model}.merge()
		attached${Model}.persist()
	}
	
	
	@RequestMapping("/$model")
	List<$Model> list() {
	  return ${Model}.findAllEntities()
	}
	
	@RequestMapping(value = "/$model/{id}", method = RequestMethod.GET)
	public get(@PathVariable Long id) {
	 	return ${Model}.findEntity(id)
	}
	
	@RequestMapping(value = "/$model/{id}", method = RequestMethod.DELETE)
	public delete(@PathVariable Long id) {
		$Model $model = get(id)
		return ${model}.remove()
	}
				 
}