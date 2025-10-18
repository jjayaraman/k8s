module "ecr" {
  source  = "terraform-aws-modules/ecr/aws"
  version = "~> 3.1.0"

  repository_name = "mongo-crud"
  repository_image_tag_mutability = "MUTABLE"

  tags = {
    Terraform   = "true"
    Environment = "dev"
    Description = "ECR repository for jay app"
  }

}
